package net.wurstclient.fmlevents;

import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class Transform extends Event {

    private final EnumHandSide enumHandSide;

    public Transform(EnumHandSide enumHandSide){
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide(){
        return this.enumHandSide;
    }
}