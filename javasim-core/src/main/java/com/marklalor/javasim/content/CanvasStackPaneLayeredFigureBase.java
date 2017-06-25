package com.marklalor.javasim.content;

import java.util.List;
import java.util.stream.Collectors;

import com.marklalor.javasim.content.interfacee.Figure;
import com.marklalor.javasim.content.interfacee.LayeredFigure;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

public abstract class CanvasStackPaneLayeredFigureBase implements LayeredFigure
{
    private ObservableList<Figure> figures;
    private StackPane node;
    
    public CanvasStackPaneLayeredFigureBase()
    {
        this.figures = FXCollections.observableArrayList();
        this.figures.addListener(this::layersChanged);
        this.node = new StackPane();
    }
    
    private void layersChanged(Change<? extends Figure> change)
    {
        syncStackPaneToFigures();
    }
    
    private void syncStackPaneToFigures()
    {
        getNode().getChildren().setAll(figures.stream().map(figure -> figure.getNode()).collect(Collectors.toList()));
    }
    
    @Override
    public StackPane getNode()
    {
        return node;
    }
    
    @Override
    public List<Figure> getLayers()
    {
        return figures;
    }
}
