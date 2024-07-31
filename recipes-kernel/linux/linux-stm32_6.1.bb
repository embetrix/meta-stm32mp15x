require linux-stm32.inc

LINUX_VERSION = "6.1.82"
SRCBRANCH = "v6.1-stm32mp"
SRCREV = "4c4175804a542f40c50d091cc694bb0b186728a0"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRC_URI += "file://defconfig"
SRC_URI += "https://cdn.kernel.org/pub/linux/kernel/projects/rt/6.1/older/patch-6.1.82-rt27.patch.gz;sha256sum=5381b4f6da5f13aa285bd980c8af695366bc3e330aa377ea0e01699270c8696f"
SRC_URI += "${@bb.utils.contains('MACHINE_FEATURES', 'rt', 'file://rt-preempt.cfg', '', d)}"
