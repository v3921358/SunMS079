package handling.world;

import java.util.Map;

public interface MapleCharacterLook {

    public byte getGender();

    public byte getSkinColor();

    public int getFace();

    public int getHair();

    public byte getSecondGender();

    public byte getSecondSkinColor();

    public int getSecondFace();

    public int getSecondHair();

    public int getFaceMarking();

    public int getEars();

    public int getTail();

    public int getElf();

    public short getJob();

    public Map<Byte, Integer> getEquips(boolean fusionAnvil);

    public Map<Byte, Integer> getSecondEquips(boolean fusionAnvil);

    public Map<Byte, Integer> getTotems();
}
