/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gatein.rest.update;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gatein.rest.constants.ConstantsService;
import org.gatein.rest.entity.Navigation;
import org.gatein.rest.entity.Node;
import org.gatein.rest.entity.Page;
import org.gatein.rest.entity.Site;
import org.gatein.rest.helper.JSonParser;
import org.gatein.rest.service.api.HelpingServiceApi;
import org.gatein.rest.service.impl.HelpingService;
import org.gatein.rest.service.impl.RestService;
import org.json.simple.parser.ParseException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mgottval
 */
public class RestUpdatePortalAll {

    private CloseableHttpClient httpclient;
    private HelpingServiceApi helpingService;
    private RestService restService;
    private HttpEntity entity;
    private ConstantsService constantsService;
    private JSonParser jSonParser = new JSonParser();

    @Before
    public void before() {
        helpingService = new HelpingService();
        httpclient = helpingService.httpClientAuthenticationRootAny();
        constantsService = new ConstantsService();
        restService = new RestService(httpclient, helpingService, constantsService);

    }

    @Test
    public void testUpdateAllSites() throws ParseException {
        //check classic updated
        String classicSite = restService.getSite("classic", "site");
        Site classic = jSonParser.siteParser(classicSite);

        assertEquals("My updated classic portal", classic.getDisplayName());
        assertEquals("This is a updated portal from a classic template", classic.getDescription());

        String classicNavigationString = restService.getNavigation("site", "classic", false);
        Navigation classicNavigation = jSonParser.navigationParser(classicNavigationString);
        List<Node> classicNavigationNodesList = classicNavigation.getNodes();
        assertTrue(classicNavigationNodesList.size() > 1);
        List<String> classicNavNodeNames = new ArrayList<>();
        for (Node node : classicNavigationNodesList) {
            classicNavNodeNames.add(node.getName());
        }

        assertTrue(classicNavNodeNames.contains("newhome"));

        String classicPagesString = restService.getSitePages("classic", "site");
        List<Page> classicPages = jSonParser.pagesParser(classicPagesString);
        assertTrue(classicPages.size() > 1);
        List<String> classicPagesNames = new ArrayList<>();
        for (Page page : classicPages) {
            classicPagesNames.add(page.getName());
        }
        assertTrue(classicPagesNames.contains("newhomepage"));

        String newHomePageString = restService.getPage("newhomepage", "classic", "site");
        Page newHomePage = jSonParser.pageParser(newHomePageString);
        assertEquals("newhomepage", newHomePage.getName());
        assertEquals("New Home Page", newHomePage.getDisplayName());

        //check mobile updated
        String mobileSite = restService.getSite("mobile", "site");
        Site mobile = jSonParser.siteParser(mobileSite);

        assertEquals("Mobile Portal", mobile.getDisplayName());
        assertEquals("Mobile Portal Template", mobile.getDescription());
        String homeNodeString = restService.getNavigationNode("site", "mobile", "home");
        Node homeNode = jSonParser.nodeParser(homeNodeString);
        List<String> displayNames = homeNode.getDisplayNames();
        assertTrue(displayNames.size() == 1);
        for (String displayname : displayNames) {
            assertEquals("Home", displayname);
        }
        String featuresNodeString = restService.getNavigationNode("site", "mobile", "features");
        Node featuresNode = jSonParser.nodeParser(featuresNodeString);
        assertEquals("Features", featuresNode.getDisplayName());
        List<String> featuresDisplayNames = featuresNode.getDisplayNames();
        assertTrue(featuresDisplayNames.size() == 1);
        for (String displayname : featuresDisplayNames) {
            assertEquals("Features", displayname);
        }
        String groupnavigationNodeString = restService.getNavigationNode("site", "mobile", "groupnavigation");
        Node groupnavigationNode = jSonParser.nodeParser(groupnavigationNodeString);
        List<String> groupnavigationDisplayNames = groupnavigationNode.getDisplayNames();
        assertTrue(groupnavigationDisplayNames.size() == 1);
        for (String displayname : groupnavigationDisplayNames) {
            assertEquals("Group Navigation", displayname);
        }
        String portalnavigationNodeString = restService.getNavigationNode("site", "mobile", "portalnavigation");
        Node portalnavigationNode = jSonParser.nodeParser(portalnavigationNodeString);
        List<String> portalnavigationDisplayNames = portalnavigationNode.getDisplayNames();
        assertTrue(portalnavigationDisplayNames.size() == 1);
        for (String displayname : portalnavigationDisplayNames) {
            assertEquals("Portal Navigation", displayname);
        }
        String registerNodeString = restService.getNavigationNode("site", "mobile", "register");
        Node registerNode = jSonParser.nodeParser(registerNodeString);
        List<String> registerDisplayNames = registerNode.getDisplayNames();
        assertTrue(registerDisplayNames.size() == 1);
        for (String displayname : registerDisplayNames) {
            assertEquals("Register", displayname);
        }

        String featuresPageString = restService.getPage("features", "mobile", "site");
        Page featuresPage = jSonParser.pageParser(featuresPageString);
        assertEquals("GateIn Features", featuresPage.getDisplayName());
    }

    @After
    public void after() {
        try {
            httpclient.close();
        } catch (IOException ex) {
            Logger.getLogger(RestUpdatePortalAll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
