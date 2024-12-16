# Android App for Aadhaar-based Age Proof 

**TLDR:** [Anon Aadhaar](https://pse.dev/en/projects/anon-aadhaar) as an Android app using [Nova](https://github.com/microsoft/Nova) instead of Groth16. Based on the Rust code at https://github.com/avras/nova-aadhaar-qr. The app is not available to the public on Google Play Store (coming soon). Try the web version [here](https://age-proof.vercel.app/).

The `kotlin` directory has the code for an Android app built using Jetpack Compose.

The `rust` directory has the glue logic needed to generate native bindings.

> [!WARNING]
> This code has not been audited. Use with care.

## Building and running the code
Run the following commands to build native bindings.

```
cd rust
./build-android.sh
```
You can then open the Android project in the `kotlin` directory using Android Studio and build it to install the app on your phone.

## License

Licensed under either of

 * Apache License, Version 2.0
   ([LICENSE-APACHE](LICENSE-APACHE) or http://www.apache.org/licenses/LICENSE-
2.0)
 * MIT license
   ([LICENSE-MIT](LICENSE-MIT) or http://opensource.org/licenses/MIT)

at your option.

## Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted
for inclusion in the work by you, as defined in the Apache-2.0 license, shall b
e
dual licensed as above, without any additional terms or conditions.
