SUMMARY = "Provides cryptographic recipes and primitives to python developers"
HOMEPAGE = "https://cryptography.io/"
SECTION = "devel/python"
LICENSE = "( Apache-2.0 | BSD-3-Clause ) & PSF-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bf405a8056a6647e7d077b0e7bc36aba \
                    file://LICENSE.APACHE;md5=4e168cce331e5c827d4c2b68a6200e1b \
                    file://LICENSE.BSD;md5=5ae30ba4123bc4f2fa49aa0b0dce887b \
                    file://LICENSE.PSF;md5=43c37d21e1dbad10cddcd150ba2c0595 \
                   "
LDSHARED += "-pthread"

SRC_URI[sha256sum] = "ae430d51c67ac638dfbb42edf56c669ca9c74744f4d225ad11c6f3d355858187"

SRC_URI += "file://run-ptest \
           file://check-memfree.py \
           file://0001-Cargo.toml-specify-pem-version.patch \
           file://0002-Cargo.toml-edition-2018-2021.patch \
           file://0001-pyproject.toml-remove-benchmark-disable-option.patch \
           crate://crates.io/Inflector/0.11.4 \
           crate://crates.io/aliasable/0.1.3 \
           crate://crates.io/asn1/0.8.7 \
           crate://crates.io/asn1_derive/0.8.7 \
           crate://crates.io/autocfg/1.1.0 \
           crate://crates.io/base64/0.13.0 \
           crate://crates.io/bitflags/1.3.2 \
           crate://crates.io/cfg-if/1.0.0 \
           crate://crates.io/chrono/0.4.19 \
           crate://crates.io/indoc-impl/0.3.6 \
           crate://crates.io/indoc/0.3.6 \
           crate://crates.io/instant/0.1.12 \
           crate://crates.io/lazy_static/1.4.0 \
           crate://crates.io/libc/0.2.124 \
           crate://crates.io/lock_api/0.4.7 \
           crate://crates.io/num-integer/0.1.44 \
           crate://crates.io/num-traits/0.2.14 \
           crate://crates.io/once_cell/1.10.0 \
           crate://crates.io/ouroboros/0.15.0 \
           crate://crates.io/ouroboros_macro/0.15.0 \
           crate://crates.io/parking_lot/0.11.2 \
           crate://crates.io/parking_lot_core/0.8.5 \
           crate://crates.io/paste-impl/0.1.18 \
           crate://crates.io/paste/0.1.18 \
           crate://crates.io/pem/1.0.2 \
           crate://crates.io/proc-macro-error-attr/1.0.4 \
           crate://crates.io/proc-macro-error/1.0.4 \
           crate://crates.io/proc-macro-hack/0.5.19 \
           crate://crates.io/proc-macro2/1.0.37 \
           crate://crates.io/pyo3-build-config/0.15.2 \
           crate://crates.io/pyo3-macros-backend/0.15.2 \
           crate://crates.io/pyo3-macros/0.15.2 \
           crate://crates.io/pyo3/0.15.2 \
           crate://crates.io/quote/1.0.18 \
           crate://crates.io/redox_syscall/0.2.13 \
           crate://crates.io/scopeguard/1.1.0 \
           crate://crates.io/smallvec/1.8.0 \
           crate://crates.io/stable_deref_trait/1.2.0 \
           crate://crates.io/syn/1.0.91 \
           crate://crates.io/unicode-xid/0.2.2 \
           crate://crates.io/unindent/0.1.8 \
           crate://crates.io/version_check/0.9.4 \
           crate://crates.io/winapi-i686-pc-windows-gnu/0.4.0 \
           crate://crates.io/winapi-x86_64-pc-windows-gnu/0.4.0 \
           crate://crates.io/winapi/0.3.9 \
           "

inherit pypi python_setuptools3_rust

DEPENDS += " \
    ${PYTHON_PN}-cffi-native \
"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-cffi \
"

RDEPENDS:${PN}:append:class-target = " \
    ${PYTHON_PN}-numbers \
    ${PYTHON_PN}-threading \
"

RDEPENDS:${PN}-ptest += " \
    ${PYTHON_PN}-bcrypt \
    ${PYTHON_PN}-cryptography-vectors (= ${PV}) \
    ${PYTHON_PN}-hypothesis \
    ${PYTHON_PN}-iso8601 \
    ${PYTHON_PN}-pretend \
    ${PYTHON_PN}-psutil \
    ${PYTHON_PN}-pytest \
    ${PYTHON_PN}-pytest-subtests \
    ${PYTHON_PN}-pytz \
    ${PYTHON_PN}-tomli \
"

inherit ptest

do_install_ptest() {
    install -D ${WORKDIR}/check-memfree.py ${D}${PTEST_PATH}/
    install -d ${D}${PTEST_PATH}/tests
    cp -rf ${S}/tests/* ${D}${PTEST_PATH}/tests/
    # remove test_x509.py as it needs benchmark and we don't
    # want to introduce the benchmark dependency
    rm -rf ${D}${PTEST_PATH}/tests/bench/test_x509.py
    install -d ${D}${PTEST_PATH}/tests/hazmat
    cp -rf ${S}/tests/hazmat/* ${D}${PTEST_PATH}/tests/hazmat/
    cp -r ${S}/pyproject.toml ${D}${PTEST_PATH}/
}

FILES:${PN}-ptest += " \
    ${PTEST_PATH}/check-memfree.py \
"
FILES:${PN}-dbg += " \
    ${PYTHON_SITEPACKAGES_DIR}/${SRCNAME}/hazmat/bindings/.debug \
"

BBCLASSEXTEND = "native nativesdk"
