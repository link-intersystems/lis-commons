package com.link_intersystems.swing.view.layout;

import com.link_intersystems.swing.view.ContainerViewContent;
import com.link_intersystems.swing.view.DefaultViewSite;
import com.link_intersystems.swing.view.View;
import com.link_intersystems.swing.view.ViewSite;
import com.link_intersystems.util.context.Context;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class DefaultViewLayout implements ViewLayout {

    private String id;
    private Map<String, ViewSite> layout = new HashMap<>();
    private Map<String, View> installedViews = new HashMap<>();
    private Context viewContext;
    private Container viewContainer;

    public DefaultViewLayout(Context viewContext, Container viewContainer) {
        this(MAIN_ID, viewContext, viewContainer);
    }

    public DefaultViewLayout(String id, Context viewContext, Container viewContainer) {
        this.id = requireNonNull(id);
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        this.viewContext = requireNonNull(viewContext);
        this.viewContainer = requireNonNull(viewContainer);
    }

    @Override
    public String getId() {
        return id;
    }

    public void addViewSite(String name, Object layoutConstraints) {
        ViewSite layoutViewSite = new DefaultViewSite(new ContainerViewContent(viewContainer, layoutConstraints), viewContext);
        layout.put(requireNonNull(name), layoutViewSite);

    }

    @Override
    public void install(String viewSiteName, View view) {
        ViewSite viewSite = layout.get(viewSiteName);
        if (viewSite == null) {
            throw new IllegalArgumentException("No viewSite named " + viewSiteName + " existent in " + this);
        }
        view.install(viewSite);
        installedViews.put(viewSiteName, view);
        viewContainer.revalidate();
    }

    @Override
    public void remove(String viewSiteName) {
        View view = installedViews.get(viewSiteName);
        if (view != null) {
            view.uninstall();
            viewContainer.revalidate();
        }
    }

    public void dispose() {
        installedViews.keySet().forEach(this::remove);
    }

    @Override
    public String toString() {
        return "DefaultViewLayout{" +
                "id='" + id + '\'' +
                '}';
    }
}