package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 07/09/24
 */
public class ClassForTestingNullValues {

    @Pibify(1)
    private String aString = null;

    @Pibify(2)
    private int anInt = 2;

    @Pibify(3)
    private long aLong = 3L;

    @Pibify(4)
    private float aFloat = 4.0f;

    @Pibify(5)
    private double aDouble = 5.0d;

    @Pibify(6)
    private boolean aBoolean = true;

    @Pibify(7)
    private char aChar = '7';

    @Pibify(8)
    private byte aByte = 8;

    @Pibify(9)
    private short aShort = 9;

    @Pibify(10)
    private Integer anIntObj = null;

    @Pibify(11)
    private Long aLongObj = null;

    @Pibify(12)
    private Float aFloatObj = null;

    @Pibify(13)
    private Double aDoubleObj = null;

    @Pibify(14)
    private Boolean aBooleanObj = null;

    @Pibify(15)
    private Character aCharObj = null;


    @Pibify(16)
    private Byte aByteObj = null;

    @Pibify(17)
    private Short aShortObj = null;

    @Pibify(18)
    private List<String> aStringList = null;

    @Pibify(19)
    private Set<Integer> anIntSet = null;

    @Pibify(20)
    private Map<Float, Boolean> aMap = null;

    @Pibify(21)
    private List<List<String>> aString1 = null;

    @Pibify(22)
    private Set<Set<Integer>> anInt1 = null;

    @Pibify(23)
    private Set<List<Integer>> collection2 = null;

    @Pibify(24)
    private List<Set<Integer>> collection3 = null;

    @Pibify(25)
    private ClassWithReferences ref1 = null;

    @Pibify(26)
    private Map<Float, Map<String, Boolean>> aMap1 = null;

    @Pibify(27)
    private Map<Float, Map<String, Map<Integer, Boolean>>> map2 = null;

    @Pibify(28)
    private Map<Map<Boolean, String>, Map<Integer, Boolean>> map3 = null;

    @Pibify(29)
    private List<Map<String, Float>> listOfMaps = null;

    @Pibify(30)
    private Set<Map<String, Float>> setOfMaps = null;

    @Pibify(31)
    private Map<List<String>, Set<Boolean>> mapOfCollections = null;

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public char getaChar() {
        return aChar;
    }

    public void setaChar(char aChar) {
        this.aChar = aChar;
    }

    public byte getaByte() {
        return aByte;
    }

    public void setaByte(byte aByte) {
        this.aByte = aByte;
    }

    public short getaShort() {
        return aShort;
    }

    public void setaShort(short aShort) {
        this.aShort = aShort;
    }

    public Integer getAnIntObj() {
        return anIntObj;
    }

    public void setAnIntObj(Integer anIntObj) {
        this.anIntObj = anIntObj;
    }

    public Long getaLongObj() {
        return aLongObj;
    }

    public void setaLongObj(Long aLongObj) {
        this.aLongObj = aLongObj;
    }

    public Float getaFloatObj() {
        return aFloatObj;
    }

    public void setaFloatObj(Float aFloatObj) {
        this.aFloatObj = aFloatObj;
    }

    public Double getaDoubleObj() {
        return aDoubleObj;
    }

    public void setaDoubleObj(Double aDoubleObj) {
        this.aDoubleObj = aDoubleObj;
    }

    public Boolean getaBooleanObj() {
        return aBooleanObj;
    }

    public void setaBooleanObj(Boolean aBooleanObj) {
        this.aBooleanObj = aBooleanObj;
    }

    public Character getaCharObj() {
        return aCharObj;
    }

    public void setaCharObj(Character aCharObj) {
        this.aCharObj = aCharObj;
    }

    public Byte getaByteObj() {
        return aByteObj;
    }

    public void setaByteObj(Byte aByteObj) {
        this.aByteObj = aByteObj;
    }

    public Short getaShortObj() {
        return aShortObj;
    }

    public void setaShortObj(Short aShortObj) {
        this.aShortObj = aShortObj;
    }

    public List<String> getaStringList() {
        return aStringList;
    }

    public void setaStringList(List<String> aStringList) {
        this.aStringList = aStringList;
    }

    public Set<Integer> getAnIntSet() {
        return anIntSet;
    }

    public void setAnIntSet(Set<Integer> anIntSet) {
        this.anIntSet = anIntSet;
    }

    public Map<Float, Boolean> getaMap() {
        return aMap;
    }

    public void setaMap(Map<Float, Boolean> aMap) {
        this.aMap = aMap;
    }

    public List<List<String>> getaString1() {
        return aString1;
    }

    public void setaString1(List<List<String>> aString1) {
        this.aString1 = aString1;
    }

    public Set<Set<Integer>> getAnInt1() {
        return anInt1;
    }

    public void setAnInt1(Set<Set<Integer>> anInt1) {
        this.anInt1 = anInt1;
    }

    public Set<List<Integer>> getCollection2() {
        return collection2;
    }

    public void setCollection2(Set<List<Integer>> collection2) {
        this.collection2 = collection2;
    }

    public List<Set<Integer>> getCollection3() {
        return collection3;
    }

    public void setCollection3(List<Set<Integer>> collection3) {
        this.collection3 = collection3;
    }

    public ClassWithReferences getRef1() {
        return ref1;
    }

    public void setRef1(ClassWithReferences ref1) {
        this.ref1 = ref1;
    }

    public Map<Float, Map<String, Boolean>> getaMap1() {
        return aMap1;
    }

    public void setaMap1(Map<Float, Map<String, Boolean>> aMap1) {
        this.aMap1 = aMap1;
    }

    public Map<Float, Map<String, Map<Integer, Boolean>>> getMap2() {
        return map2;
    }

    public void setMap2(Map<Float, Map<String, Map<Integer, Boolean>>> map2) {
        this.map2 = map2;
    }

    public Map<Map<Boolean, String>, Map<Integer, Boolean>> getMap3() {
        return map3;
    }

    public void setMap3(Map<Map<Boolean, String>, Map<Integer, Boolean>> map3) {
        this.map3 = map3;
    }

    public List<Map<String, Float>> getListOfMaps() {
        return listOfMaps;
    }

    public void setListOfMaps(List<Map<String, Float>> listOfMaps) {
        this.listOfMaps = listOfMaps;
    }

    public Set<Map<String, Float>> getSetOfMaps() {
        return setOfMaps;
    }

    public void setSetOfMaps(Set<Map<String, Float>> setOfMaps) {
        this.setOfMaps = setOfMaps;
    }

    public Map<List<String>, Set<Boolean>> getMapOfCollections() {
        return mapOfCollections;
    }

    public void setMapOfCollections(Map<List<String>, Set<Boolean>> mapOfCollections) {
        this.mapOfCollections = mapOfCollections;
    }
}
