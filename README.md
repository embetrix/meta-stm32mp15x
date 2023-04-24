# meta-stm32mp15x

## Summary

**meta-stm32mp15x** is a yocto layer containing BSP support for the [stm32mp15x](https://wiki.st.com/stm32mpu/wiki/Category:STM32MP15x) based devices.

This layer relies on OpenEmbedded/Yocto build system and depends on:

```
[OECORE]
URI: https://git.yoctoproject.org/git/poky.git
layers: meta
branch: same dedicated branch as meta-stm32mp15x
```
and

```
[OE]
URI: https://github.com/openembedded/meta-openembedded.git
layers: meta-oe
branch: same dedicated branch as meta-stm32mp15x
```

## Build

This layer can be integrated in your layers or built standalone using [kas-tool](https://github.com/siemens/kas):

```
kas build kas-stm32mp15x.yml
```

or using kas docker container:

```
kas-container build kas-stm32mp15x.yml
```

Flash image on a SDCard using [bmap-tools](https://github.com/intel/bmap-tools):

    $ sudo bmaptool copy build/tmp/deploy/images/stm32mp157f-dk2/stm32mp15x-demo-image-stm32mp157f-dk2.wic.gz /dev/mmcblk0

## List of tested boards:

* [stm32mp157c-dk2](https://www.st.com/en/evaluation-tools/stm32mp157c-dk2.html)
* [stm32mp157f-dk2](https://www.st.com/en/evaluation-tools/stm32mp157f-dk2.html)

## Contributing

If you want to contribute changes, you can send Github pull requests at:
https://github.com/embetrix/meta-stm32mp15x/pulls


## Maintainers

 - Ayoub Zaki <info@embetrix.com>
