uniffi::setup_scaffolding!();

#[uniffi::export]
fn hello_world() -> String {
    "Hello from Rust on Android!".to_string()
}
