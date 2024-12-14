// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorSumupPlugin6",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "CapacitorSumupPlugin6",
            targets: ["SumUpPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "SumUpPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/SumUpPlugin"),
        .testTarget(
            name: "SumUpPluginTests",
            dependencies: ["SumUpPlugin"],
            path: "ios/Tests/SumUpPluginTests")
    ]
)