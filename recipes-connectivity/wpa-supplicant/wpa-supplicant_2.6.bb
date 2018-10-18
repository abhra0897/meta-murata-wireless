SUMMARY = "Client for Wi-Fi Protected Access (WPA)"
HOMEPAGE = "http://w1.fi/wpa_supplicant/"
BUGTRACKER = "http://w1.fi/security/"
SECTION = "network"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=292eece3f2ebbaa25608eed8464018a3 \
                    file://README;beginline=1;endline=56;md5=3f01d778be8f953962388307ee38ed2b \
                    file://wpa_supplicant/wpa_supplicant.c;beginline=1;endline=12;md5=4061612fc5715696134e3baf933e8aba"
DEPENDS = "dbus libnl"
RRECOMMENDS_${PN} = "wpa-supplicant-passphrase wpa-supplicant-cli"

PACKAGECONFIG ??= "gnutls"
PACKAGECONFIG[gnutls] = ",,gnutls libgcrypt"
PACKAGECONFIG[openssl] = ",,openssl"

inherit systemd

SYSTEMD_SERVICE_${PN} = "wpa_supplicant.service wpa_supplicant-nl80211@.service wpa_supplicant-wired@.service"
SYSTEMD_AUTO_ENABLE = "disable"

SRC_URI = "http://w1.fi/releases/wpa_supplicant-${PV}.tar.gz  \
           file://defconfig \
           file://wpa-supplicant.sh \
           file://wpa_supplicant.conf \
           file://wpa_supplicant.conf-sane \
           file://99_wpa_supplicant \
           file://0001-hostapd-Avoid-key-reinstallation-in-FT-handshake.patch;apply=yes \
           file://0002-Prevent-reinstallation-of-an-already-in-use-group-ke.patch;apply=yes \
           file://0003-Extend-protection-of-GTK-IGTK-reinstallation-of-WNM-.patch;apply=yes \
           file://0004-Prevent-installation-of-an-all-zero-TK.patch;apply=yes \
           file://0005-Fix-PTK-rekeying-to-generate-a-new-ANonce.patch;apply=yes \
           file://0006-TDLS-Reject-TPK-TK-reconfiguration.patch;apply=yes \
           file://0007-WNM-Ignore-WNM-Sleep-Mode-Response-without-pending-r.patch;apply=yes \
           file://0008-FT-Do-not-allow-multiple-Reassociation-Response-fram.patch;apply=yes \
           file://0009-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
           file://0010-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
           file://0011-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
           file://0012-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
           file://0013-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
           file://0014-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
           file://0015-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
           file://0016-driver_nl80211-support-passing-PSK-on-connect.patch;apply=yes \
           file://0017-driver_nl80211-check-4-way-handshake-offload-support.patch;apply=yes \
           file://0018-nl80211-Add-API-to-set-the-PMK-to-the-driver.patch;apply=yes \
           file://0019-driver-Add-port-authorized-event.patch;apply=yes \
           file://0020-nl80211-Handle-port-authorized-event.patch;apply=yes \
           file://0021-wpa_supplicant-Handle-port-authorized-event.patch;apply=yes \
           file://0022-murata-wpa_supplication-Add-server-in-hs20.patch;apply=yes \
	   file://0022-wpa_supplicant-Notify-Neighbor-Report-for-driver-tri.patch;apply=yes \
           file://0023-driver_nl80211-Fix-802.1X-auth-failure-when-offloadi.patch;apply=yes \
           file://0024-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
    	   file://0025-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
    	   file://0026-Sync-with-mac80211-next.git-include-uapi-linux-nl802.patch;apply=yes \
    	   file://0027-wpa_supplicant-SAE-Allow-SAE-password-to-be-configured-separately-S.patch;apply=yes \
    	   file://0028-wpa_supplicant-Fix-sae_password-documentation-in-wpa_supplicant-to-.patch;apply=yes \
    	   file://0029-wpa_supplicant-Add-more-debug-prints-for-wpa_sm_set_pmk-calls.patch;apply=yes \
    	   file://0030-wpa_supplicant-SAE-Fix-default-PMK-configuration-for-PMKSA-caching-.patch;apply=yes \
    	   file://0031-nl80211-Use-RSN_AUTH_KEY_MGMT_-instead-of-WLAN_AKM_S.patch;apply=yes \
    	   file://0032-non-upstream-Sync-with-Linux-kernel-nl80211.h-for-SA.patch;apply=yes \
    	   file://0033-nl80211-Check-SAE-authentication-offload-support.patch;apply=yes \
    	   file://0034-SAE-Pass-SAE-password-on-connect-for-SAE-authenticat.patch;apply=yes \
    	   file://0035-WPA-Ignore-unauthenticated-encrypted-EAPOL-Key-data.patch;apply=yes \
"

SRC_URI[md5sum] = "091569eb4440b7d7f2b4276dbfc03c3c"
SRC_URI[sha256sum] = "b4936d34c4e6cdd44954beba74296d964bc2c9668ecaa5255e499636fe2b1450"

S = "${WORKDIR}/wpa_supplicant-${PV}"

PACKAGES_prepend = "wpa-supplicant-passphrase wpa-supplicant-cli "
FILES_wpa-supplicant-passphrase = "${bindir}/wpa_passphrase"
FILES_wpa-supplicant-cli = "${sbindir}/wpa_cli"
FILES_${PN} += "${datadir}/dbus-1/system-services/*"
CONFFILES_${PN} += "${sysconfdir}/wpa_supplicant.conf"

do_configure () {
	${MAKE} -C wpa_supplicant clean
	install -m 0755 ${WORKDIR}/defconfig wpa_supplicant/.config
	echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> wpa_supplicant/.config
	echo "DRV_CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> wpa_supplicant/.config
	
	if echo "${PACKAGECONFIG}" | grep -qw "openssl"; then
        	ssl=openssl
	elif echo "${PACKAGECONFIG}" | grep -qw "gnutls"; then
        	ssl=gnutls
	fi
	if [ -n "$ssl" ]; then
        	sed -i "s/%ssl%/$ssl/" wpa_supplicant/.config
	fi

	# For rebuild
	rm -f wpa_supplicant/*.d wpa_supplicant/dbus/*.d
}

export EXTRA_CFLAGS = "${CFLAGS}"
export BINDIR = "${sbindir}"

do_compile () {
	echo "Compiling: "
        echo "ARCH: ${ARCH} "
        echo "CROSS_COMPILE: ${CROSS_COMPILE} "
	unset CFLAGS CPPFLAGS CXXFLAGS
	sed -e "s:CFLAGS\ =.*:& \$(EXTRA_CFLAGS):g" -i ${S}/src/lib.rules
        
	oe_runmake -C wpa_supplicant
}

do_install () {
	install -d ${D}${sbindir}
	install -m 755 wpa_supplicant/wpa_supplicant ${D}${sbindir}
	install -m 755 wpa_supplicant/wpa_cli        ${D}${sbindir}

	install -d ${D}${bindir}
	install -m 755 wpa_supplicant/wpa_passphrase ${D}${bindir}

	install -d ${D}${docdir}/wpa_supplicant
	install -m 644 wpa_supplicant/README ${WORKDIR}/wpa_supplicant.conf ${D}${docdir}/wpa_supplicant

	install -d ${D}${sysconfdir}
	install -m 600 ${WORKDIR}/wpa_supplicant.conf-sane ${D}${sysconfdir}/wpa_supplicant.conf

	install -d ${D}${sysconfdir}/network/if-pre-up.d/
	install -d ${D}${sysconfdir}/network/if-post-down.d/
	install -d ${D}${sysconfdir}/network/if-down.d/
	install -m 755 ${WORKDIR}/wpa-supplicant.sh ${D}${sysconfdir}/network/if-pre-up.d/wpa-supplicant
	cd ${D}${sysconfdir}/network/ && \
	ln -sf ../if-pre-up.d/wpa-supplicant if-post-down.d/wpa-supplicant

	install -d ${D}/${sysconfdir}/dbus-1/system.d
	install -m 644 ${S}/wpa_supplicant/dbus/dbus-wpa_supplicant.conf ${D}/${sysconfdir}/dbus-1/system.d
	install -d ${D}/${datadir}/dbus-1/system-services
	install -m 644 ${S}/wpa_supplicant/dbus/*.service ${D}/${datadir}/dbus-1/system-services

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -d ${D}/${systemd_unitdir}/system
		install -m 644 ${S}/wpa_supplicant/systemd/*.service ${D}/${systemd_unitdir}/system
	fi

	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_wpa_supplicant ${D}/etc/default/volatiles
}

pkg_postinst_wpa-supplicant () {
	# If we're offline, we don't need to do this.
	if [ "x$D" = "x" ]; then
		killall -q -HUP dbus-daemon || true
	fi

}
