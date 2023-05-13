package com.link_intersystems.swing.text;

import java.awt.*;
import java.util.EventListener;
import java.util.List;

public interface LineSupportListener extends EventListener {

    public void onLineYOffsetChanged(List<Integer> lineYOffsets);

    public void onFontChange(Font font);

}
