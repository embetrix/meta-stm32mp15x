SUMMARY = "OP-TEE sanity testsuite"
HOMEPAGE = "https://github.com/OP-TEE/optee_test"

LICENSE = "BSD-2-Clause & GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/LICENSE.md;md5=daa2bcccc666345ab8940aab1315a4fa"

SRC_URI = "git://github.com/OP-TEE/optee_test.git;protocol=https;branch=master"
SRC_URI += "file://0001-no-error-deprecated-declarations.patch"
SRC_URI += "file://0002-ta-os_test-skip-bget-test-when-pager-is-constrained-.patch"
SRC_URI += "file://0003-regression-1013-lower-number-of-loops-when-pager-is-.patch"
SRC_URI += "file://0004-ta-crypt-remove-CFG_SYSTEM_PTA-ifdef.patch"
SRC_URI += "file://0005-regression-4012-4016-remove-CFG_SYSTEM_PTA-dependenc.patch"
SRC_URI += "file://0006-xtest-remove-CFG_SECSTOR_TA_MGMT_PTA-dependency.patch"

SRCREV = "ab9863cc187724e54c032b738c28bd6e9460a4db"

PV = "3.19.0+git${SRCPV}"

S = "${WORKDIR}/git"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "optee-client virtual/optee-os python3-pycryptodomex-native libgcc"
DEPENDS += "openssl"
DEPENDS += "python3-cryptography-native"

inherit python3native cmake

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
EXTRA_OECMAKE = "-DOPTEE_TEST_SDK=${TA_DEV_KIT_DIR} \
                 -DCFG_TEE_CLIENT_LOAD_PATH=${libdir} \
                 -DCFG_TEE_PLUGIN_LOAD_PATH=${TEE_PLUGIN_LOAD_PATH} \
                 "

do_compile:prepend() {
    export CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_HOST}"
    export OPENSSL_MODULES=${STAGING_LIBDIR_NATIVE}/ossl-modules/
}

do_compile:append() {
    cd ${S}
    oe_runmake ta
}

do_install:append () {
    # install path should match the value set in optee-client/tee-supplicant
    # default TEEC_LOAD_PATH is /lib
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz/
    install -D -p -m0444 ${S}/out/ta/*/*.ta ${D}${nonarch_base_libdir}/optee_armtz/
}

FILES:${PN} += "${nonarch_base_libdir}/optee_armtz/"
FILES:${PN} += "${TEE_PLUGIN_LOAD_PATH}/"
