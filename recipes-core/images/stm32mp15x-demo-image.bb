DESCRIPTION = "A STM32MP15x Demo Image"

inherit core-image ima-evm-sign

IMAGE_FEATURES += "ssh-server-openssh read-only-rootfs"

IMAGE_INSTALL += "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    ${CORE_IMAGE_BASE_INSTALL} \
    openssl-bin \
    opensc \
    libp11 \
    softhsm \
    curl \
    strace \
    gdbserver \
    lvgl-demo \
    cryptsetup \
    keyutils \
    ima-evm-utils \
    "
