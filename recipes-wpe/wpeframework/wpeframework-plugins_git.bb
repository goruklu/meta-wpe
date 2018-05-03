SUMMARY = "WPE Framework common plugins"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"
PR = "r1"

require wpeframework-plugins.inc

SRC_URI = "git://git@github.com/WebPlatformForEmbedded/WPEFrameworkPlugins.git;protocol=ssh;branch=master \
          file://index.html \
          file://0003-RemoteControl-Snapshot-Fix-refsw-include-paths.patch \
"

SRCREV = "2064d88e2bf76686fe9102e60f16992249ca7830"

WEBKITBROWSER_AUTOSTART ?= "true"
WEBKITBROWSER_MEDIADISKCACHE ?= "false"
WEBKITBROWSER_MEMORYPRESSURE ?= "databaseprocess:50m,networkprocess:100m,webprocess:300m,rpcprocess:50m"
WEBKITBROWSER_MEMORYPROFILE ?= "128m"
WEBKITBROWSER_STARTURL ?= "file:///var/www/index.html"
WEBKITBROWSER_USERAGENT ?= "Mozilla/5.0 (Macintosh, Intel Mac OS X 10_11_4) AppleWebKit/602.1.28+ (KHTML, like Gecko) Version/9.1 Safari/601.5.17"
WEBKITBROWSER_DISKCACHE ?= "0"
WEBKITBROWSER_XHRCACHE ?= "false"
WEBKITBROWSER_TRANSPARENT ?= "false"

WPEFRAMEWORK_LOCATIONSYNC_URI ?= "http://jsonip.metrological.com/?maf=true"
WPEFRAMEWORK_REMOTECONTROL_NEXUS_IRMODE ?= "16"
WPEFRAMEWORK_REMOTECONTROL_KEYMAP = "OSMCKeyMap.json"

WPEFRAMEWORK_PLUGIN_WEBSERVER_PORT ?= "8080"
WPEFRAMEWORK_PLUGIN_WEBSERVER_PATH ?= "/var/www/"

WPE_WIFI ?= "${@bb.utils.contains('MACHINE_FEATURES', 'wifi', 'wifi', '', d)}"

# Snapshot only works on BRCM STBs and RPIs
WPE_SNAPSHOT ?= ""
WPE_SNAPSHOT_rpi = "snapshot"
WPE_SNAPSHOT_DEP = "${@bb.utils.contains('PREFERRED_PROVIDER_virtual/egl', 'broadcom-refsw', 'broadcom-refsw', 'userland', d)}"

## Compositor settings, if Wayland is in the distro set the implementation to Wayland with Westeros dependency
WPE_COMPOSITOR ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'compositor', '', d)}"
WPE_COMPOSITOR_IMPL = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'Wayland', 'None', d)}"
WPE_COMPOSITOR_DEP = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'westeros', '', d)}"

# override to nexus for bcm nexus enabled builds
WPE_COMPOSITOR_nexus = "compositor"
WPE_COMPOSITOR_IMPL_nexus = "Nexus"
WPE_COMPOSITOR_DEP_nexus = "broadcom-refsw"

# if wpeframework is in distro features, take control over certain system specific features such as network, timesync and compositing
WPE_FRAMEWORK ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wpeframework', '${WPE_COMPOSITOR} locationsync network timesync ${WPE_WIFI}', '', d)}"

# OpenCDM switches
# opencdmi_ck = clearkey, opencdmi_pr = playready, opencdmi_wv = widevine
WPEFRAMEWORK_OCDM_PKG = "opencdmi opencdmi_ck opencdmi_pr"

# PACAKAGE CONFIG
PACKAGECONFIG ?= "deviceinfo monitor ${WPEFRAMEWORK_OCDM_PKG} remote remote-uinput ${WPE_SNAPSHOT} tracing ux virtualinput webkitbrowser webserver ${WPE_FRAMEWORK} youtube"

PACKAGECONFIG[commander]      = "-DWPEFRAMEWORK_PLUGIN_COMMANDER=ON,-DWPEFRAMEWORK_PLUGIN_COMMANDER=OFF,"
PACKAGECONFIG[compositor]     = "-DWPEFRAMEWORK_PLUGIN_COMPOSITOR=ON -DWPEFRAMEWORK_PLUGIN_COMPOSITOR_IMPLEMENTATION=${WPE_COMPOSITOR_IMPL} -DWPEFRAMEWORK_PLUGIN_COMPOSITOR_VIRTUALINPUT=ON,-DWPEFRAMEWORK_PLUGIN_COMPOSITOR=OFF,${WPE_COMPOSITOR_DEP}"
PACKAGECONFIG[deviceinfo]     = "-DWPEFRAMEWORK_PLUGIN_DEVICEINFO=ON,-DWPEFRAMEWORK_PLUGIN_DEVICEINFO=OFF,"
PACKAGECONFIG[locationsync]   = "-DWPEFRAMEWORK_PLUGIN_LOCATIONSYNC=ON \
   -DWPEFRAMEWORK_PLUGIN_LOCATIONSYNC_URI=${WPEFRAMEWORK_LOCATIONSYNC_URI} \
   ,-DWPEFRAMEWORK_PLUGIN_LOCATIONSYNC=OFF,"
PACKAGECONFIG[network]        = "-DWPEFRAMEWORK_PLUGIN_NETWORKCONTROL=ON,-DWPEFRAMEWORK_PLUGIN_NETWORKCONTROL=OFF,"
PACKAGECONFIG[monitor]        = "-DWPEFRAMEWORK_PLUGIN_MONITOR=ON \
                                 -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEMORYLIMIT=614400 \
                                 -DWPEFRAMEWORK_PLUGIN_YOUTUBE_MEMORYLIMIT=614400 \
                                 -DWPEFRAMEWORK_PLUGIN_NETFLIX_MEMORYLIMIT=307200 \
                                ,-DWPEFRAMEWORK_PLUGIN_MONITOR=OFF,"
PACKAGECONFIG[opencdmi]       = "-DWPEFRAMEWORK_PLUGIN_OPENCDMI=ON \
                                 -DWPEFRAMEWORK_PLUGIN_OPENCDMI_AUTOSTART=true \
                                 -DWPEFRAMEWORK_PLUGIN_OPENCDMI_OOP=true \
                                ,,"
PACKAGECONFIG[opencdmi_ck]    = "-DPLUGIN_OPENCDMI_CLEARKEY=ON,,"
PACKAGECONFIG[opencdmi_pr]    = "-DPLUGIN_OPENCDMI_PLAYREADY=ON,,"
PACKAGECONFIG[opencdmi_prnx]  = "-DPLUGIN_OPENCDMI_PLAYREADY_NEXUS=ON,,"
PACKAGECONFIG[opencdmi_wv]    = "-DPLUGIN_OPENCDMI_WIDEVINE=ON,,"
PACKAGECONFIG[remote]         = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL=ON \
                                 -DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_KEYMAP=${WPEFRAMEWORK_REMOTECONTROL_KEYMAP} \
                                ,-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL=OFF,"
PACKAGECONFIG[remote-nexus]   = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_IRNEXUS=ON \
                                 -DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_IRMODE=${WPEFRAMEWORK_REMOTECONTROL_IRMODE} \
                                ,-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_IRNEXUS=OFF,broadcom-refsw"
PACKAGECONFIG[remote-uinput]  = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_DEVINPUT=ON,-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_DEVINPUT=OFF,"
PACKAGECONFIG[snapshot]       = "-DWPEFRAMEWORK_PLUGIN_SNAPSHOT=ON,-DWPEFRAMEWORK_PLUGIN_SNAPSHOT=OFF,${WPE_SNAPSHOT_DEP} libpng"
PACKAGECONFIG[timesync]       = "-DWPEFRAMEWORK_PLUGIN_TIMESYNC=ON,-DWPEFRAMEWORK_PLUGIN_TIMESYNC=OFF,"
PACKAGECONFIG[tracing]        = "-DWPEFRAMEWORK_PLUGIN_TRACECONTROL=ON,-DWPEFRAMEWORK_PLUGIN_TRACECONTROL=OFF,"
PACKAGECONFIG[ux]             = "-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_UX=ON,-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_UX=ON,"
PACKAGECONFIG[virtualinput]   = "-DWPEFRAMEWORK_PLUGIN_COMPOSITOR_VIRTUALINPUT=ON,-DWPEFRAMEWORK_PLUGIN_COMPOSITOR_VIRTUALINPUT=OFF,"
PACKAGECONFIG[webkitbrowser]  = "-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER=ON \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_AUTOSTART="${WEBKITBROWSER_AUTOSTART}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEDIADISKCACHE="${WEBKITBROWSER_MEDIADISKCACHE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEMORYPRESSURE="${WEBKITBROWSER_MEMORYPRESSURE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEMORYPROFILE="${WEBKITBROWSER_MEMORYPROFILE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_STARTURL="${WEBKITBROWSER_STARTURL}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_USERAGENT="${WEBKITBROWSER_USERAGENT}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_DISKCACHE="${WEBKITBROWSER_DISKCACHE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_XHRCACHE="${WEBKITBROWSER_XHRCACHE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_TRANSPARENT="${WEBKITBROWSER_TRANSPARENT}" \
   ,-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER=OFF,wpewebkit"
PACKAGECONFIG[webproxy]       = "-DWPEFRAMEWORK_PLUGIN_WEBPROXY=ON,-DWPEFRAMEWORK_PLUGIN_WEBPROXY=OFF,"
PACKAGECONFIG[webserver]      = "-DWPEFRAMEWORK_PLUGIN_WEBSERVER=ON \
    -DWPEFRAMEWORK_WEBSERVER_PORT="${WPEFRAMEWORK_PLUGIN_WEBSERVER_PORT}" \
    -DWPEFRAMEWORK_WEBSERVER_PATH="${WPEFRAMEWORK_PLUGIN_WEBSERVER_PATH}" \
    ,-DWPEFRAMEWORK_PLUGIN_WEBSERVER=OFF,"
PACKAGECONFIG[webshell]       = "-DWPEFRAMEWORK_PLUGIN_WEBSHELL=ON,-DWPEFRAMEWORK_PLUGIN_WEBSHELL=OFF,"
PACKAGECONFIG[wifi]           = "-DWPEFRAMEWORK_PLUGIN_WIFICONTROL=ON,-DWPEFRAMEWORK_PLUGIN_WIFICONTROL=OFF,,wpa-supplicant"
PACKAGECONFIG[youtube]        = "-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_YOUTUBE=ON, -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_YOUTUBE=OFF,,wpeframework-dialserver"


EXTRA_OECMAKE += " \
    -DBUILD_REFERENCE=${SRCREV} \
    -DBUILD_SHARED_LIBS=ON \
"
do_install_append() {
    if ${@bb.utils.contains("PACKAGECONFIG", "webserver", "true", "false", d)}
    then
      if ${@bb.utils.contains("PACKAGECONFIG", "webkitbrowser", "true", "false", d)}
      then
          install -d ${D}/var/www
          install -m 0755 ${WORKDIR}/index.html ${D}/var/www/
      fi
      install -d ${D}${WPEFRAMEWORK_PLUGIN_WEBSERVER_PATH}
    fi
}

# ----------------------------------------------------------------------------

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/wpeframework/plugins/*.so ${libdir}/libwaylandeglclient.so ${datadir}/WPEFramework/* /var/www/index.html"
FILES_${PN}-dbg += "${datadir}/WPEFramework/WebKitBrowser/.debug/libWPEInjectedBundle.so"

INSANE_SKIP_${PN} += "libdir staticdev"
