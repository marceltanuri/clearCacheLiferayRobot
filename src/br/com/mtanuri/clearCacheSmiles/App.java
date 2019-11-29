package br.com.mtanuri.clearCacheSmiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.jsoup.nodes.Element;

public class App {

	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {

		LOGGER.info("Started: " + LOGGER.getName());

		PropertiesUtil propertiesUtil = null;

		try {
			propertiesUtil = new PropertiesUtil();
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}

		final String siteDomain = propertiesUtil.getPropertie("site.domain");
		final String clearDbCacheUrl = propertiesUtil.getPropertie("site.dbcache.url");
		final String ipGetterUrl = propertiesUtil.getPropertie("site.ipgetter.url");
		final String logoutUrl = propertiesUtil.getPropertie("site.logout.url");
		final String loginURL = propertiesUtil.getPropertie("site.login.url");
		final int maxAttempts = Integer.valueOf(propertiesUtil.getPropertie("app.attempts.max"));
		final int clusterSize = Integer.valueOf(propertiesUtil.getPropertie("app.cluster.size"));

		LOGGER.info("************************************************************************************************"
				+ "\nHi! I'm Nicolas, the clearing Smiles cache robot!"
				+ "\nI'm going to help you by clearing DataBase Cache of all cluster nodes at Smiles site."
				+ "\nBe sure you are connected at SmilesVPN."
				+ "\nIf you get some handshake error try changing your internet connection from wi-fi to 4G."
				+ "\nNice to meet you! :)"
				+ "\n************************************************************************************************");

		LOGGER.info("Executing clearing at " + siteDomain + " ...");

		HashMap<String, String> map = new HashMap<String, String>();

		int attempts = 0;
		while (map.size() <= clusterSize && attempts < maxAttempts) {

			try {

				DoGET ipGetterPage = new DoGET(ipGetterUrl);
				Element currentNodeDOM = ipGetterPage.getDoc().selectFirst("#computer-name");
				String currentNode = currentNodeDOM.text();

				if (!map.containsKey(currentNode)) {

					LOGGER.info("Signing in " + currentNode + " ...");

					HashMap<String, String> formData = new HashMap<>();
					formData.put("_58_login", propertiesUtil.getPropertie("app.auth.username"));
					formData.put("_58_password", propertiesUtil.getPropertie("app.auth.password"));

					DoPOST loginAction = new DoPOST(loginURL, ipGetterPage.getCookies(), formData);

					boolean loginSuccess = !loginAction.getDoc().title()
							.contains(propertiesUtil.getPropertie("app.login.success.probe"));
					if (loginSuccess)
						LOGGER.info("Login success!");
					else
						LOGGER.warning("Login failed");

					if (loginSuccess) {
						LOGGER.info("Clearing DB Cache on " + currentNode + " ...");
						formData = new HashMap<>();
						formData.put("_137_cmd", "cacheDb");

						int indexOf = loginAction.getDoc().head().html().indexOf("Liferay.authToken");
						String p_auth = loginAction.getDoc().head().html().substring(indexOf + 21, indexOf + 29);

						new DoPOST(clearDbCacheUrl.replace("$P_AUTH", p_auth), ipGetterPage.getCookies(), formData);

						LOGGER.info("Signing out " + currentNode + " ...");
						new DoGET(logoutUrl, ipGetterPage.getCookies());
					}

					map.put(currentNode, currentNode);
				}

			}

			catch (java.net.SocketTimeoutException e) {
				LOGGER.warning("time out");
			}

			catch (org.jsoup.HttpStatusException e) {
				LOGGER.warning("504");
			}

			catch (IOException e) {
				LOGGER.warning("connection error");
			}

			attempts++;
		}

		LOGGER.info("Finished with " + attempts + " attempts. Number of accessed nodes: " + String.valueOf(map.size()));
	}
}
