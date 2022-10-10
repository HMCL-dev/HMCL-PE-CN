package net.matrix.mobile_hiper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.matrix.mobile_hiper.utils.StringUtils;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class Sites {

    /**
     * configuration example
     *
     *
     * # This is the hiper minimization configuration file. - (x.x.x.x/x)
     * pki:
     *   ca: "-----BEGIN HIPER CERTIFICATE-----\n first line \n second line \n third line \n-----END HIPER CERTIFICATE-----\n"
     *   cert: "-----BEGIN HIPER CERTIFICATE-----\n first line \n second line \n third line \n forth line \n-----END HIPER CERTIFICATE-----\n"
     *   key: "-----BEGIN HIPER X25519 PRIVATE KEY-----\n first line \n-----END HIPER X25519 PRIVATE KEY-----\n"
     *
     * # --------------------------------------------------------------------------------------
     * #                        WARNING >>> AUTO SYNC AREA
     * # --------------------------------------------------------------------------------------
     * # The following configuration will change at any time.
     * # Please do not configure custom content in the above area.
     * # If you need to adjust the configuration, please modify the menu to manual mode.
     * point:
     *   "x.x.x.x":
     *     - "ip : port"
     *     - "ip : port"
     *   "x.x.x.x":
     *     - "ip : port"
     *   "x.x.x.x":
     *     - "ip : port"
     *   "x.x.x.x":
     *     - "ip : port"
     *   "x.x.x.x":
     *     - "ip : port"
     *   "x.x.x.x":
     *     - "ip : port"
     *   "x.x.x.x":
     *     - "ip : port"
     *   "x.x.x.x":
     *     - "ip : port"
     *   "x.x.x.x":
     *     - "ip : port"
     *   "x.x.x.x":
     *     - "ip : port"
     *
     * tower:
     *   hosts:
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *
     * relay:
     *   relays:
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     *     - "x.x.x.x"
     * # --------------------------------------------------------------------------------------
     * #                        WARNING <<< AUTO SYNC AREA
     * # --------------------------------------------------------------------------------------
     */

    public static class IncomingSite{

        private final String name;
        private final String id;
        private HashMap<String, StaticHosts> point;
        private final ArrayList<UnsafeRoute> unsafeRoutes;
        private final String cert;
        private final String ca;
        private final int lhDuration;
        private final int port;
        private final int mtu;
        private final String cipher;
        private final int sortKey;
        private final String logVerbosity;
        @Expose(serialize = false)
        private String key;

        public IncomingSite() {
            this("", "", new HashMap<>(), new ArrayList<>(), "", "", 0, 0, 0, "", 0, "", "");
        }

        public IncomingSite(String name, String id, HashMap<String, StaticHosts> point, ArrayList<UnsafeRoute> unsafeRoutes, String cert, String ca, int lhDuration, int port, int mtu, String cipher, int sortKey, String logVerbosity, String key) {
            this.name = name;
            this.id = id;
            this.point = point;
            this.unsafeRoutes = unsafeRoutes;
            this.cert = cert;
            this.ca = ca;
            this.lhDuration = lhDuration;
            this.port = port;
            this.mtu = mtu;
            this.cipher = cipher;
            this.sortKey = sortKey;
            this.logVerbosity = logVerbosity;
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public HashMap<String, StaticHosts> getPoint() {
            return point;
        }

        public ArrayList<UnsafeRoute> getUnsafeRoutes() {
            return unsafeRoutes;
        }

        public String getCert() {
            return cert;
        }

        public String getCa() {
            return ca;
        }

        public int getLhDuration() {
            return lhDuration;
        }

        public int getPort() {
            return port;
        }

        public int getMtu() {
            return mtu;
        }

        public String getCipher() {
            return cipher;
        }

        public int getSortKey() {
            return sortKey;
        }

        public String getLogVerbosity() {
            return logVerbosity;
        }

        public void save(Context context) {
            String path = context.getFilesDir().getAbsolutePath() + "/hiper/hiper_config.json";
            StringUtils.writeFile(path, new Gson().toJson(this));

            String keyPath = context.getFilesDir().getAbsolutePath() + "/hiper/hiper.key";
            StringUtils.writeFile(keyPath, key);
        }

        public void update(String conf) {
            Yaml yaml = new Yaml();
            Map object = yaml.load(conf);
            HashMap<String, ArrayList<String>> rawPoint = (HashMap<String, ArrayList<String>>) object.get("point");
            HashMap<String, ArrayList<String>> tower = (HashMap<String, ArrayList<String>>) object.get("tower");
            ArrayList<String> hosts = tower.get("hosts");
            HashMap<String, StaticHosts> point = new HashMap<>();
            for (String pointKey : rawPoint.keySet()) {
                boolean isTower = hosts.contains(pointKey);
                StaticHosts staticHosts = new StaticHosts(isTower, rawPoint.get(pointKey));
                point.put(pointKey, staticHosts);
            }
            this.point = point;
        }

        public static IncomingSite parse(String conf, String id) {
            Yaml yaml = new Yaml();
            Map object = yaml.load(conf);
            HashMap<String, String> pki = (HashMap<String, String>) object.get("pki");
            String cert = pki.get("cert");
            String ca = pki.get("ca");
            String key = pki.get("key");
            HashMap<String, ArrayList<String>> rawPoint = (HashMap<String, ArrayList<String>>) object.get("point");
            HashMap<String, ArrayList<String>> tower = (HashMap<String, ArrayList<String>>) object.get("tower");
            ArrayList<String> hosts = tower.get("hosts");
            HashMap<String, StaticHosts> point = new HashMap<>();
            for (String pointKey : rawPoint.keySet()) {
                boolean isTower = hosts.contains(pointKey);
                StaticHosts staticHosts = new StaticHosts(isTower, rawPoint.get(pointKey));
                point.put(pointKey, staticHosts);
            }
            return new IncomingSite(
                    "HMCL-PE-Multiplayer-Sesson",
                    id,
                    point,
                    new ArrayList<>(),
                    cert,
                    ca,
                    0,
                    0,
                    1300,
                    "aes",
                    0,
                    "info",
                    key
            );
        }

    }

    public static class Site {

        private final String name;
        private final String id;
        private final HashMap<String, StaticHosts> point;
        private final ArrayList<UnsafeRoute> unsafeRoutes;
        private final CertificateInfo cert;
        private final ArrayList<CertificateInfo> ca;
        private final int lhDuration;
        private final int port;
        private final int mtu;
        private final String cipher;
        private final int sortKey;
        private final String logVerbosity;
        private final boolean connected;
        private final String status;
        private final String logFile;
        private final ArrayList<String> errors;

        // Strong representation of the site config
        @Expose(serialize = false)
        private final String config;

        public Site() {
            this("", "", new HashMap<>(), new ArrayList<>(), new CertificateInfo(), new ArrayList<>(), 0, 0, 0, "", 0, "", false, "", "", new ArrayList<>(), "");
        }

        public Site(String name, String id, HashMap<String, StaticHosts> point, ArrayList<UnsafeRoute> unsafeRoutes, CertificateInfo cert, ArrayList<CertificateInfo> ca, int lhDuration, int port, int mtu, String cipher, int sortKey, String logVerbosity, boolean connected, String status, String logFile, ArrayList<String> errors, String config) {
            this.name = name;
            this.id = id;
            this.point = point;
            this.unsafeRoutes = unsafeRoutes;
            this.cert = cert;
            this.ca = ca;
            this.lhDuration = lhDuration;
            this.port = port;
            this.mtu = mtu;
            this.cipher = cipher;
            this.sortKey = sortKey;
            this.logVerbosity = logVerbosity;
            this.connected = connected;
            this.status = status;
            this.logFile = logFile;
            this.errors = errors;
            this.config = config;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public HashMap<String, StaticHosts> getPoint() {
            return point;
        }

        public ArrayList<UnsafeRoute> getUnsafeRoutes() {
            return unsafeRoutes;
        }

        public CertificateInfo getCert() {
            return cert;
        }

        public ArrayList<CertificateInfo> getCa() {
            return ca;
        }

        public int getLhDuration() {
            return lhDuration;
        }

        public int getPort() {
            return port;
        }

        public int getMtu() {
            return mtu;
        }

        public String getCipher() {
            return cipher;
        }

        public int getSortKey() {
            return sortKey;
        }

        public String getLogVerbosity() {
            return logVerbosity;
        }

        public boolean isConnected() {
            return connected;
        }

        public String getStatus() {
            return status;
        }

        public String getLogFile() {
            return logFile;
        }

        public ArrayList<String> getErrors() {
            return errors;
        }

        public String getConfig() {
            return config;
        }

        public String getKey(Context context) {
            String keyPath = context.getFilesDir().getAbsolutePath() + "/hiper/hiper.key";
            return StringUtils.getStringFromFile(keyPath);
        }

        public static Site fromFile(Context context) {
            String path = context.getFilesDir().getAbsolutePath() + "/hiper/hiper_config.json";
            if (!new File(path).exists()) {
                return null;
            }
            String s = StringUtils.getStringFromFile(path);
            IncomingSite incomingSite = new Gson().fromJson(s, IncomingSite.class);
            ArrayList<String> errors = new ArrayList<>();
            CertificateInfo cert = new CertificateInfo();
            ArrayList<CertificateInfo> ca = new ArrayList<>();
            try {
                String rawDetails = mobileHiPer.MobileHiPer.parseCerts(incomingSite.cert);
                CertificateInfo[] certs = new Gson().fromJson(rawDetails, CertificateInfo[].class);
                if (certs.length == 0) {
                    errors.add("No certificate found");
                }
                cert = certs[0];
                if (!cert.getValidity().isValid()) {
                    errors.add("Certificate is invalid: " + cert.getValidity().getReason());
                }
            } catch (Exception e) {
                e.printStackTrace();
                errors.add(e.toString());
            }
            try {
                String rawCa = mobileHiPer.MobileHiPer.parseCerts(incomingSite.getCa());
                CertificateInfo[] caArray = new Gson().fromJson(rawCa, CertificateInfo[].class);
                ca = new ArrayList<>(Arrays.asList(caArray));
                boolean hasErrors = false;
                for (CertificateInfo info : ca) {
                    if (!info.getValidity().isValid()) {
                        hasErrors = true;
                        break;
                    }
                }
                if (hasErrors) {
                    errors.add("There are issues with 1 or more ca certificates");
                }
            } catch (Exception e) {
                e.printStackTrace();
                errors.add("Error while loading certificate authorities: " + e);
            }
            return new Site(
                    incomingSite.getName(),
                    incomingSite.getId(),
                    incomingSite.getPoint(),
                    incomingSite.getUnsafeRoutes(),
                    cert,
                    ca,
                    incomingSite.getLhDuration(),
                    incomingSite.getPort(),
                    incomingSite.getMtu(),
                    incomingSite.getCipher(),
                    incomingSite.getSortKey(),
                    incomingSite.getLogVerbosity(),
                    false,
                    "Disconnected",
                    context.getFilesDir().getAbsolutePath() + "/hiper/hiper.log",
                    errors,
                    s
            );
        }

    }

    public static class CertificateDetails {

        private final String name;
        private final String notBefore;
        private final String notAfter;
        private final String publicKey;
        private final List<String> groups;
        private final List<String> ips;
        private final List<String> subnets;
        private final boolean isCa;
        private final String issuer;

        public CertificateDetails() {
            this("", "", "", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, "");
        }

        public CertificateDetails(String name, String notBefore, String notAfter, String publicKey, List<String> groups, List<String> ips, List<String> subnets, boolean isCa, String issuer) {
            this.name = name;
            this.notBefore = notBefore;
            this.notAfter = notAfter;
            this.publicKey = publicKey;
            this.groups = groups;
            this.ips = ips;
            this.subnets = subnets;
            this.isCa = isCa;
            this.issuer = issuer;
        }

        public String getName() {
            return name;
        }

        public String getNotBefore() {
            return notBefore;
        }

        public String getNotAfter() {
            return notAfter;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public List<String> getGroups() {
            return groups;
        }

        public List<String> getIps() {
            return ips;
        }

        public List<String> getSubnets() {
            return subnets;
        }

        public boolean isCa() {
            return isCa;
        }

        public String getIssuer() {
            return issuer;
        }

    }

    public static class Certificate {

        private final String fingerprint;
        private final String signature;
        private final CertificateDetails details;

        public Certificate() {
            this("", "", new CertificateDetails());
        }

        public Certificate(String fingerprint, String signature, CertificateDetails details) {
            this.fingerprint = fingerprint;
            this.signature = signature;
            this.details = details;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public String getSignature() {
            return signature;
        }

        public CertificateDetails getDetails() {
            return details;
        }

    }

    public static class CertificateValidity {

        @SerializedName("Valid")
        private final boolean valid;
        @SerializedName("Reason")
        private final String reason;

        public CertificateValidity() {
            this(false, "");
        }

        public CertificateValidity(boolean valid, String reason) {
            this.valid = valid;
            this.reason = reason;
        }

        public boolean isValid() {
            return valid;
        }

        public String getReason() {
            return reason;
        }

    }

    public static class CertificateInfo {

        @SerializedName("Cert")
        private final Certificate cert;
        @SerializedName("RawCert")
        private final String rawCert;
        @SerializedName("Validity")
        private final CertificateValidity validity;

        public CertificateInfo() {
            this(new Certificate(), "", new CertificateValidity());
        }

        public CertificateInfo(Certificate cert, String rawCert, CertificateValidity validity) {
            this.cert = cert;
            this.rawCert = rawCert;
            this.validity = validity;
        }

        public Certificate getCert() {
            return cert;
        }

        public String getRawCert() {
            return rawCert;
        }

        public CertificateValidity getValidity() {
            return validity;
        }

    }

    public static class StaticHosts {

        private final boolean tower;
        private final List<String> destinations;

        public StaticHosts() {
            this(false, new ArrayList<>());
        }

        public StaticHosts(boolean tower, List<String> destinations) {
            this.tower = tower;
            this.destinations = destinations;
        }

        public boolean isTower() {
            return tower;
        }

        public List<String> getDestinations() {
            return destinations;
        }

    }

    public static class UnsafeRoute {

        private final String route;
        private final String via;
        private final int mtu;

        public UnsafeRoute() {
            this("", "", 0);
        }

        public UnsafeRoute(String route, String via, int mtu) {
            this.route = route;
            this.via = via;
            this.mtu = mtu;
        }

        public String getRoute() {
            return route;
        }

        public String getVia() {
            return via;
        }

        public int getMtu() {
            return mtu;
        }

    }

}
