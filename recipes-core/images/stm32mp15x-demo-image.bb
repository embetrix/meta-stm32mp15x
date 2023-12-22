DESCRIPTION = "A STM32MP15x Demo Image"

IMAGE_FEATURES += "ssh-server-openssh"

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
    "

inherit core-image
