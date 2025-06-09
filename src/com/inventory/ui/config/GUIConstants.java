/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.ui.config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 *
 * @author USER
 */
public class GUIConstants {
    private static final String FONT_STRING = "Segoe UI";
    
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    public static final Color TEXT_COLOR = new Color(51, 51, 51);
    public static final Color ACCENT_COLOR = new Color(52, 152, 219); // Blue
    public static final Color BUTTON_HOVER_COLOR = new Color(41, 128, 185); // Darker Blue
    public static final Color BORDER_COLOR = new Color(204, 204, 204);
    public static final Color ERROR_COLOR = new Color(231, 76, 60); // Red for errors
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113); // Green for success

    // Fonts
    public static final Font LABEL_FONT = new Font(FONT_STRING, Font.PLAIN, 14);
    public static final Font TEXT_FIELD_FONT = new Font(FONT_STRING, Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font(FONT_STRING, Font.BOLD, 14);
    public static final Font MESSAGE_FONT = new Font(FONT_STRING, Font.ITALIC, 12);
    public static final Font TITLE_FONT = new Font(FONT_STRING, Font.BOLD, 24);
    
    public static final Dimension SEARCH_FIELD_DIMENSION = new Dimension(720, 25);
}