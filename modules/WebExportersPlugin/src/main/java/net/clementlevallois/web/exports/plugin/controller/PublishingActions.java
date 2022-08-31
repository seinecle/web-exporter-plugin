/*
 * author: Clément Levallois
 */
package net.clementlevallois.web.exports.plugin.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author LEVALLOIS
 */
public class PublishingActions {

    public static JsonObject publishToRetinaViaNocodeFunctions(String fileName) {
        boolean testLocal = false;
        JsonObject jsonObject;

        try {
            ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
            if (pc.getCurrentProject() == null) {
                jsonObject = new JsonObject();
                jsonObject.addProperty("95", "create or open a project with a network");
                return jsonObject;
            }
            GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
            if (graphModel.getGraph().getNodeCount() == 0) {
                jsonObject = new JsonObject();
                jsonObject.addProperty("96", "your network is empty - please add nodes before publishing");
                return jsonObject;
            }
            Workspace workspace = pc.getCurrentWorkspace();
            ExportController ec = Lookup.getDefault().lookup(ExportController.class);
            Exporter exporterGexf = ec.getExporter("gexf");
            exporterGexf.setWorkspace(workspace);
            StringWriter stringWriter = new StringWriter();
            ec.exportWriter(stringWriter, (CharacterExporter) exporterGexf);
            String gexfToSendAsString = stringWriter.toString();

            HttpClient client = HttpClient.newHttpClient();
            String url;
            if (testLocal) {
                url = "http://localhost:4242/gephi-viewer/api/";
            } else {
                url = "https://test.nocodefunctions.com/gephi-viewer/api/";
            }
            if (!fileName.endsWith(".gexf")) {
                fileName = fileName + ".gexf";
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(gexfToSendAsString))
                    .header("filename", fileName)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBodyAsString = response.body();
            System.out.println("response: " + responseBodyAsString);
            JsonElement responseAsJsonElement = JsonParser.parseString(responseBodyAsString);
            jsonObject = responseAsJsonElement.getAsJsonObject();
        } catch (IOException | InterruptedException ex) {
            Exceptions.printStackTrace(ex);
            jsonObject = new JsonObject();
        }
        return jsonObject;
    }

}
