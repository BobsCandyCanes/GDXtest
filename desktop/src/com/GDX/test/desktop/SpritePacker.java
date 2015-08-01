package com.GDX.test.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class SpritePacker 
{
    public static void main (String[] args) throws Exception 
    {
        TexturePacker.process("input", "output", "spritesheet");
    }
}
