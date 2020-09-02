package client;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.life.MapleLifeFactory;
import server.life.MapleMonsterStats;
import server.maps.AnimatedMapleMapObject;
import server.maps.MapleMapObjectType;
import server.movement.LifeMovement;
import server.movement.LifeMovementFragment;
import server.movement.StaticLifeMovement;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CField;
import tools.packet.PacketHelper;

public final class MonsterFamiliar extends AnimatedMapleMapObject
        implements Serializable {

    private static final long serialVersionUID = 795419937713738569L;
    private final int id;
    private final int familiar;
    private int fatigue;
    private final int characterid;
    private String name;
    private long expiry;
    private short fh = 0;
    private byte vitality;

    public MonsterFamiliar(int characterid, int id, int familiar, long expiry, String name, int fatigue, byte vitality) {
        this.familiar = familiar;
        this.characterid = characterid;
        this.expiry = expiry;
        this.vitality = vitality;
        this.id = id;
        this.name = name;
        this.fatigue = fatigue;
        setStance(0);
        setPosition(new Point(0, 0));
    }

    public MonsterFamiliar(int characterid, int familiar, long expiry) {
        this.familiar = familiar;
        this.characterid = characterid;
        this.expiry = expiry;
        fatigue = 0;
        vitality = 1;
        name = getOriginalName();
        id = Randomizer.nextInt();
    }

    public String getOriginalName() {
        return getOriginalStats().getName();
    }

    public MapleMonsterStats getOriginalStats() {
        return MapleLifeFactory.getMonsterStats(MapleItemInformationProvider.getInstance().getFamiliar(familiar).mob);
    }

    public void addFatigue(MapleCharacter owner) {
        addFatigue(owner, 1);
    }

    public void addFatigue(MapleCharacter owner, int f) {
        fatigue = Math.min(vitality * 300, Math.max(0, fatigue + f));
        owner.getClient().getSession().write(CField.updateFamiliar(this));
        if (fatigue >= vitality * 300) {
            owner.removeFamiliar();
        }
    }

    public int getFamiliar() {
        return familiar;
    }

    public int getId() {
        return id;
    }

    public int getFatigue() {
        return fatigue;
    }

    public int getCharacterId() {
        return characterid;
    }

    public final String getName() {
        return name;
    }

    public long getExpiry() {
        return expiry;
    }

    public byte getVitality() {
        return vitality;
    }

    public void setFatigue(int f) {
        fatigue = f;
    }

    public void setName(String n) {
        name = n;
    }

    public void setExpiry(long e) {
        expiry = e;
    }

    public void setVitality(int v) {
        vitality = ((byte) v);
    }

    public void setFh(int f) {
        fh = ((short) f);
    }

    public short getFh() {
        return fh;
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        client.getSession().write(CField.spawnFamiliar(this, true, false));
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.getSession().write(CField.spawnFamiliar(this, false, false));
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.FAMILIAR;
    }

    public final void updatePosition(List<LifeMovementFragment> movement) {
        for (LifeMovementFragment move : movement) {
            if (((move instanceof LifeMovement))
                    && ((move instanceof StaticLifeMovement))) {
                setFh(((StaticLifeMovement) move).getUnk());
            }
        }
    }

    public void writeRegisterPacket(MaplePacketLittleEndianWriter mplew, boolean chr) {
        mplew.writeInt(getCharacterId());
        mplew.writeInt(getFamiliar());
        mplew.writeZeroBytes(13);
        mplew.write(chr ? 1 : 0);
        mplew.writeShort(getVitality());
        mplew.writeInt(getFatigue());
        mplew.writeLong(PacketHelper.getTime(getVitality() >= 3 ? System.currentTimeMillis() : -2L));
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeLong(PacketHelper.getTime(getExpiry()));
        mplew.writeShort(getVitality());
    }
}
