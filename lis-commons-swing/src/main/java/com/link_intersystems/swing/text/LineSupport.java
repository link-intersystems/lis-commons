package com.link_intersystems.swing.text;

import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public interface LineSupport {
    public void addLineSupportListener(LineSupportListener changeListener);
    public void removeLineSupportListener(LineSupportListener changeListener);
    List<Integer> getYLineOffsets();
    Font getFont();
    Border getBorder();
    public Rectangle getBounds();
}

