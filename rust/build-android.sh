#!/bin/bash

# Script taken from https://forgen.tech/en/blog/post/building-an-android-app-with-rust-using-uniffi

# Set up cargo-ndk and add the Android targets
cargo install cargo-ndk
rustup target add aarch64-linux-android armv7-linux-androideabi i686-linux-android x86_64-linux-android
 
# Build the dylib (macOS) or so (Linux)
cargo build --release
 
# Build the Android libraries in jniLibs
cargo ndk -o ../kotlin/app/src/main/jniLibs \
        --manifest-path ./Cargo.toml \
        -t armeabi-v7a \
        -t arm64-v8a \
        -t x86 \
        -t x86_64 \
        build --release
 
# Create Kotlin bindings
# For macOS, change extension of libageproof to dylib
cargo run --release --bin uniffi-bindgen generate --library ./target/release/libageproof.so --language kotlin --out-dir ../kotlin/app/src/main/java/aadhaar/ageproof/rust