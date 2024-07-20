SUMMARY = "stm32mp15x Initramfs image"

inherit core-image

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""

PACKAGE_INSTALL = "initramfs-init base-files base-passwd ${VIRTUAL-RUNTIME_base-utils} ${ROOTFS_BOOTSTRAP_INSTALL}"
		
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

IMAGE_LINK_NAME = "${IMAGE_BASENAME}"
