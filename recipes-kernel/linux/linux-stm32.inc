DESCRIPTION = "STM32 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION_EXTENSION = ""

PV = "${LINUX_VERSION}+git${SRCPV}"

KCONFIG_MODE="--alldefconfig"
COMPATIBLE_MACHINE = "(stm32mp15x)"

KERNEL_VERSION_SANITY_SKIP="1"

SRC_URI  = "git://github.com/STMicroelectronics/linux.git;protocol=https;branch=${SRCBRANCH}"
SRC_URI += "file://fbcon.c.patch"
SRC_URI += "file://defconfig"
SRC_URI += "file://embetrix.png"

do_add_logo[depends] += "imagemagick-native:do_populate_sysroot"
addtask add_logo after do_patch before do_compile

do_add_logo () {

    convert.im7 -colors 224 -format ppm -compress none \
                ${WORKDIR}/embetrix.png \
                ${STAGING_KERNEL_DIR}/drivers/video/logo/logo_linux_clut224.ppm
}

kernel_do_configure:prepend() {

    if [ -f "${KMOD_SIGN_KEY}" -a -f "${KMOD_SIGN_CERT}" ]; then
        cat "${KMOD_SIGN_KEY}" "${KMOD_SIGN_CERT}" > "${B}/modsign_key.pem"
    else
        bberror "kmod-sign: key or certificate are invalid!"
    fi
}

do_shared_workdir:append() {

    if [ -f "${KMOD_SIGN_KEY}" -a -f "${KMOD_SIGN_CERT}" ]; then
        cp "${B}/modsign_key.pem" $kerneldir/
    else
        bberror "kmod-sign: key or certificate are invalid!"
    fi
}
