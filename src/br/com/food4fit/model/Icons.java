package br.com.food4fit.model;

import javafx.scene.image.ImageView;

public class Icons {
	private ImageView image;
    private int id;

    Icons(ImageView img)
    {
        this.image = img;
    }

    public void setImage(ImageView value)
    {
        image = value;
    }

    public ImageView getImage()
    {
        return image;
    }

    public void setSring(int id)
    {
        this.id = id;
    }

    public int getString()
    {
        return this.id;
    }





}
