SUMMARY = "OP-TEE sanity testsuite"
HOMEPAGE = "https://github.com/OP-TEE/optee_test"

LICENSE = "BSD-2-Clause & GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/LICENSE.md;md5=daa2bcccc666345ab8940aab1315a4fa"

SRC_URI = "git://github.com/OP-TEE/optee_test.git;protocol=https;branch=master"
SRCREV = "1cf0e6d2bdd1145370033d4e182634458528579d"

PV = "3.16.0+git${SRCPV}"

S = "${WORKDIR}/git"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "optee-client virtual/optee-os python3-pycryptodomex-native libgcc"
DEPENDS += "openssl"
DEPENDS += "python3-cryptography-native"

inherit python3native

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TEEC_EXPORT         = "${STAGING_DIR_HOST}${prefix}"
TA_DEV_KIT_DIR      = "${STAGING_INCDIR}/optee/export-user_ta"
TEE_PLUGIN_LOAD_PATH = "${libdir}/tee-supplicant/plugins"

EXTRA_OEMAKE = " TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
                 OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
                 TEEC_EXPORT=${TEEC_EXPORT} \
                 CROSS_COMPILE_HOST=${TARGET_PREFIX} \
                 CROSS_COMPILE_TA=${TARGET_PREFIX} \
                 V=1 \
                 CFG_TEE_CLIENT_LOAD_PATH=${libdir} \
                 CFG_TEE_PLUGIN_LOAD_PATH=${TEE_PLUGIN_LOAD_PATH} \
                 DESTDIR=${D} \
               "

do_compile:prepend() {
    export CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_HOST}"
    export OPENSSL_MODULES=${STAGING_LIBDIR_NATIVE}/ossl-modules/
}

do_install () {
    install -D -p -m0755 ${B}/out/xtest/xtest ${D}${bindir}/xtest

    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz/
    install -D -p -m0444 ${B}/out/ta/*/*.ta ${D}${nonarch_base_libdir}/optee_armtz/
    mkdir -p ${D}${libdir}/tee-supplicant/plugins
    install -D -p -m0444 ${B}/out/supp_plugin/*.plugin ${D}${libdir}/tee-supplicant/plugins/
}

FILES:${PN} += "${nonarch_base_libdir}/optee_armtz/"
FILES:${PN} += "${TEE_PLUGIN_LOAD_PATH}/"
