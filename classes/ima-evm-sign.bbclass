inherit image_types

DEPENDS += "ima-evm-utils-native"

EXTRA_IMAGECMD:ext4 = "-b 2048 -i 4096"

ima_evm_sign () {

    evmctl -r sign --imahash -k "${IMA_SIGN_KEY}" ${IMAGE_ROOTFS}
}

IMAGE_PREPROCESS_COMMAND:append = "ima_evm_sign"
