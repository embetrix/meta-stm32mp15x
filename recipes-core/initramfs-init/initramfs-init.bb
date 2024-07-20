SUMMARY = "initramfs init scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://initramfs-init.sh"

S = "${WORKDIR}"

inherit allarch

RDEPENDS:${PN} = "				 \
				busybox			 \
				ima-evm-utils	 \
				util-linux-mount \
				u-boot-fw-utils	 \
				"

FILES:${PN} += "/dev			\
				${base_sbindir} \
				${sysconfdir}   \
				"

do_install() {

		install -d ${D}${base_sbindir}
		install -m 0755 ${WORKDIR}/initramfs-init.sh ${D}${base_sbindir}/init

		install -d ${D}/dev
		mknod -m 622 ${D}/dev/console c 5 1
}

do_install:append() {

	install -d ${D}${sysconfdir}/keys
	install -m 0644 ${IMA_SIGN_CERT}	${D}${sysconfdir}/keys/x509_ima.der
	install -m 0644 ${IMA_SIGN_CERT}	${D}${sysconfdir}/keys/x509_evm.der
#	install -m 0644 ${EVM_KMK_KEY}		${D}${sysconfdir}/keys/kmk
#	install -m 0644 ${EVM_KEY}			${D}${sysconfdir}/keys/evm-key
}