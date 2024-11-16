use base64::{prelude::BASE64_STANDARD, Engine as Base64Engine};
use nova_aadhaar_qr::{
    circuit::AadhaarAgeProofCircuit,
    qr::{parse_aadhaar_qr_data, DOB_LENGTH_BYTES},
};
use nova_snark::{
    provider::{PallasEngine, VestaEngine},
    traits::{circuit::TrivialCircuit, snark::RelaxedR1CSSNARKTrait, Engine},
    CompressedSNARK, PublicParams, RecursiveSNARK,
};
use sha2::{Digest, Sha256};

uniffi::setup_scaffolding!();

type E1 = PallasEngine;
type E2 = VestaEngine;
type EE1 = nova_snark::provider::ipa_pc::EvaluationEngine<E1>;
type EE2 = nova_snark::provider::ipa_pc::EvaluationEngine<E2>;
type S1 = nova_snark::spartan::snark::RelaxedR1CSSNARK<E1, EE1>;
type S2 = nova_snark::spartan::snark::RelaxedR1CSSNARK<E2, EE2>;
type C1 = AadhaarAgeProofCircuit<<E1 as Engine>::Scalar>;
type C2 = TrivialCircuit<<E2 as Engine>::Scalar>;

#[uniffi::export]
fn generate_public_parameters() -> Vec<u8> {
    let circuit_primary: C1 = AadhaarAgeProofCircuit::default();
    let circuit_secondary: C2 = TrivialCircuit::default();

    let pp = PublicParams::<E1, E2, C1, C2>::setup(
        &circuit_primary,
        &circuit_secondary,
        &*S1::ck_floor(),
        &*S2::ck_floor(),
    )
    .unwrap();

    let serialized_pp = bincode::serialize(&pp).unwrap();
    serialized_pp
}

#[derive(uniffi::Record)] 

pub struct AadhaarAgeProof {
    pub success: bool,
    pub message: String,
    pub version: u32,
    pub pp_hash: String,
    pub num_steps: u32,
    pub current_date_ddmmyyyy: String, // Current date in DD-MM-YYYY format
    pub snark_proof: String,
}

#[uniffi::export]
fn generate_proof(pp_bytes: Vec<u8>, qr_data_bytes: Vec<u8>, current_date_bytes: Vec<u8>) -> AadhaarAgeProof {
    let mut invalid_proof = AadhaarAgeProof {
        success: false,
        message: "Proof generation failed".to_string(),
        version: 1,
        pp_hash: String::new(),
        num_steps: 0,
        current_date_ddmmyyyy: String::new(),
        snark_proof: String::new(),
    };

    if current_date_bytes.len() != DOB_LENGTH_BYTES {
        invalid_proof.message = String::from("Date string invalid");
        return invalid_proof;
    }

    let pp_bytes_vec = pp_bytes.to_vec();
    let res = bincode::deserialize(&pp_bytes_vec[..]);
    if res.is_err() {
        invalid_proof.message = String::from("Public parameters deserialization failed");
        return invalid_proof;
    }
    let pp: PublicParams<E1, E2, C1, C2> = res.unwrap();

    let res = parse_aadhaar_qr_data(qr_data_bytes.to_vec());
    if res.is_err() {
        invalid_proof.message = String::from("QR Code parsing failed");
        return invalid_proof;
    }
    let aadhaar_qr_data = res.unwrap();
    let primary_circuit_sequence = C1::new_state_sequence(&aadhaar_qr_data);

    let z0_primary = C1::calc_initial_primary_circuit_input(&current_date_bytes);
    let z0_secondary = vec![<E2 as Engine>::Scalar::zero()];

    let circuit_secondary: C2 = TrivialCircuit::default();

    // produce a recursive SNARK
    let mut recursive_snark: RecursiveSNARK<E1, E2, C1, C2> =
        RecursiveSNARK::<E1, E2, C1, C2>::new(
            &pp,
            &primary_circuit_sequence[0],
            &circuit_secondary,
            &z0_primary,
            &z0_secondary,
        )
        .unwrap();

    for (i, circuit_primary) in primary_circuit_sequence.iter().enumerate() {
        let res = recursive_snark.prove_step(&pp, circuit_primary, &circuit_secondary);
        if res.is_err() {
            invalid_proof.message = String::from(format!("Recursive SNARK step {} failed", i));
            return invalid_proof;
        }
        // assert!(res.is_ok());
    }

    // verify the recursive SNARK
    let num_steps = primary_circuit_sequence.len();
    let res = recursive_snark.verify(&pp, num_steps, &z0_primary, &z0_secondary);
    if res.is_err() {
        invalid_proof.message = String::from("Recursive SNARK verification failed");
        return invalid_proof;
    }
    // assert!(res.is_ok());

    // produce a compressed SNARK
    let (pk, _vk) = CompressedSNARK::<_, _, _, _, S1, S2>::setup(&pp).unwrap();

    let res = CompressedSNARK::<_, _, _, _, S1, S2>::prove(&pp, &pk, &recursive_snark);
    if res.is_err() {
        invalid_proof.message = String::from("Compressed SNARK prove step failed");
        return invalid_proof;
    }
    // assert!(res.is_ok());

    let compressed_snark = res.unwrap();
    let snark_proof_bytes = bincode::serialize(&compressed_snark).unwrap();
    let snark_proof = BASE64_STANDARD.encode(snark_proof_bytes);

    let mut hasher = Sha256::new();
    hasher.update(pp_bytes_vec);
    let pp_hash_bytes: [u8; 32] = hasher.finalize().try_into().unwrap();
    let pp_hash = hex::encode(pp_hash_bytes);

    let nova_aadhaar_proof = AadhaarAgeProof {
        success: true,
        message: "Proof generation succeeded".to_string(),
        version: 1,
        pp_hash,
        num_steps: num_steps.try_into().unwrap(),
        current_date_ddmmyyyy: String::from_utf8(current_date_bytes.to_vec()).unwrap(),
        snark_proof,
    };

    return nova_aadhaar_proof;
}
