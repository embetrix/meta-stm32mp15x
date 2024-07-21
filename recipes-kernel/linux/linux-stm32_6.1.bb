require linux-stm32.inc

LINUX_VERSION = "6.1.28"
SRCBRANCH = "v6.1-stm32mp"
SRCREV = "7928f69738d2e57ee2a0dba6e9e680a3bf75ded9"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRC_URI += "file://defconfig"
SRC_URI += "https://cdn.kernel.org/pub/linux/kernel/projects/rt/6.1/older/patch-6.1.28-rt10.patch.gz;sha256sum=e3686855cd31a6856d40ea3601422e5ebb073d4733263c1f1e7d506a84dcd6c6"
SRC_URI += "${@bb.utils.contains('MACHINE_FEATURES', 'rt', 'file://rt-preempt.cfg', '', d)}"
