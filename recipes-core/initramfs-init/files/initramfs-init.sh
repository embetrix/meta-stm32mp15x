#!/bin/sh -ex

PATH=/sbin:/bin:/usr/sbin:/usr/bin

CMDLINE=""
ROOT_MNT="/mnt"
DATA_MNT="/var/data"
IMA_CERT="/etc/keys/x509_ima.der"
EVM_CERT="/etc/keys/x509_evm.der"
KMK_KEY="${DATA_MNT}/kmk"
EVM_KEY="${DATA_MNT}/evm-key"
INIT="/lib/systemd/systemd"

mount -t devtmpfs none   /dev
mount -t tmpfs    tmp    /tmp
mount -t proc     proc   /proc
mount -t sysfs    sysfs  /sys

#Extract root device from kernel cmdline
for c in "$(cat /proc/cmdline)"; do
	if [ "${c:0:5}" == "root=" ]; then
		ROOT_DEV="${c:5}"
	fi
done

mkdir -p ${ROOT_MNT} ${DATA_MNT}

mount ${ROOT_DEV} ${ROOT_MNT}
mount /dev/mmcblk0p8  ${DATA_MNT}

mount -t securityfs securityfs /sys/kernel/security
keyctl link @us @s

if [ ! -f ${KMK_KEY} ] || [ ! -f ${EVM_KEY} ] ; then 
	keyctl add user kmk "`dd if=/dev/urandom bs=1 count=32 2>/dev/null`" @u
	keyctl pipe `keyctl search @u user kmk` > ${KMK_KEY}
	keyctl add encrypted evm-key "new user:kmk 32" @u
	keyctl pipe `keyctl search @u encrypted evm-key` >  ${EVM_KEY}
	#echo -n "Fix rootfs..."
	#evmctl -r ima_fix ${ROOT_MNT} 2>/dev/null
	#echo "Ok."
	sync
else
	cat ${KMK_KEY} | keyctl padd user kmk @u
	keyctl add encrypted evm-key "load `cat ${EVM_KEY}`" @u 
fi

evmctl import $IMA_CERT $(keyctl newring _ima @u)
evmctl import $EVM_CERT $(keyctl newring _evm @u)

echo 3 > /sys/kernel/security/evm

mount --move /dev  ${ROOT_MNT}/dev
mount --move /tmp  ${ROOT_MNT}/tmp
mount --move /proc ${ROOT_MNT}/proc
mount --move /sys  ${ROOT_MNT}/sys

exec switch_root ${ROOT_MNT} ${INIT}
