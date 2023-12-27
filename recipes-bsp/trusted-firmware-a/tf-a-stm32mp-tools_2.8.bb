require tf-a-stm32mp-common.inc

do_compile() {

    oe_runmake certtool fiptool
}

do_install() {
                                          
    install -d ${D}${bindir}

    install -m 0755 ${B}/tools/fiptool/fiptool         ${D}${bindir}
    install -m 0755 ${B}/tools/cert_create/cert_create ${D}${bindir}
}

FILES:${PN} = "${bindir}"
BBCLASSEXTEND = "native nativesdk"
