#####################################################################################
#
# Brief	        : Sign  tf-a and fip binaries for STM32MP15x
#
# Documentation : https://wiki.st.com/stm32mpu/wiki/TF-A_BL2_Trusted_Board_Boot
#                 https://wiki.st.com/stm32mpu/wiki/How_to_secure_STM32_MPU
# 
# Author        : ayoub.zaki@embetrix.com
#
#####################################################################################

DEPENDS += "openssl-native util-linux-native tf-a-stm32mp-tools-native stm32mp-keygen-native"

do_tfa_sign() {

    stm32-sign  -k "${SECBOOT_SIGN_KEY}" \
                -s ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX} \
                -o ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/${TF_A_BASENAME}-${TFA_DEVICETREE}.${TF_A_SUFFIX}
}

do_fip_sign() {

    install -d  ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs
    cert_create --rot-key "${SECBOOT_SIGN_KEY}" -n                                                              \
                --tfw-nvctr          0                                                                          \
                --ntfw-nvctr         0                                                                          \
                --key-alg            ecdsa                                                                      \
                --hash-alg           sha256                                                                     \
                --tos-fw             ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_HEADER}-${OPTEE_CONF}.${OPTEE_SUFFIX}    \
                --tos-fw-extra1      ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGER}-${OPTEE_CONF}.${OPTEE_SUFFIX}     \
                --tos-fw-extra2      ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGEABLE}-${OPTEE_CONF}.${OPTEE_SUFFIX}  \
                --nt-fw              ${DEPLOY_DIR_IMAGE}/u-boot-nodtb-${MACHINE}.bin                            \
                --hw-config          ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.dtb                                  \
                --fw-config          ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/fdts/${TFA_DEVICETREE}-fw-config.dtb \
                --tos-fw-cert        ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/tee-header_v2.crt              \
                --trusted-key-cert   ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/trusted-key-cert.key-crt       \
                --tos-fw-key-cert    ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/tee-header_v2.key-crt          \
                --nt-fw-cert         ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/u-boot.crt                     \
                --nt-fw-key-cert     ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/u-boot.key-crt                 \
                --stm32mp-cfg-cert   ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/stm32mp_cfg_cert.crt

    fiptool update ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/${FIP_BASENAME}.bin                                    \
                --tos-fw             ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_HEADER}-${OPTEE_CONF}.${OPTEE_SUFFIX}    \
                --tos-fw-extra1      ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGER}-${OPTEE_CONF}.${OPTEE_SUFFIX}     \
                --tos-fw-extra2      ${DEPLOY_DIR_IMAGE}/optee/${OPTEE_PAGEABLE}-${OPTEE_CONF}.${OPTEE_SUFFIX}  \
                --nt-fw              ${DEPLOY_DIR_IMAGE}/u-boot-nodtb-${MACHINE}.bin                            \
                --hw-config          ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.dtb                                  \
                --fw-config          ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/fdts/${TFA_DEVICETREE}-fw-config.dtb \
                --tos-fw-cert        ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/tee-header_v2.crt              \
                --trusted-key-cert   ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/trusted-key-cert.key-crt       \
                --tos-fw-key-cert    ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/tee-header_v2.key-crt          \
                --nt-fw-cert         ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/u-boot.crt                     \
                --nt-fw-key-cert     ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/u-boot.key-crt                 \
                --stm32mp-cfg-cert   ${B}/build/stm32mp1/${TFA_BUILD_TYPE}/certs/stm32mp_cfg_cert.crt           
}

do_deploy:append() {

    openssl ec -in ${SECBOOT_SIGN_KEY}  -outform PEM -out ${DEPLOYDIR}/secureboot-pubkey.pem -pubout
    ecdsa-sha256 --public-key=${DEPLOYDIR}/secureboot-pubkey.pem \
                 --binhash-file=${DEPLOYDIR}/secureboot-pubhash.bin
    
    # Generate u-boot cmd to fuse public key hashes into OTP
    echo fuse prog -y 0 0x18 $(hexdump -e '/4 "0x"' -e '/1 "%x"' -e '" "'\
                 ${DEPLOYDIR}/secureboot-pubhash.bin) > ${DEPLOYDIR}/u-boot-fuse-prog.txt
}

addtask do_tfa_sign after do_compile
addtask do_fip_sign after do_compile
addtask deploy before do_package after do_fip_sign
addtask deploy before do_package after do_tfa_sign
