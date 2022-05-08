package com.enn.energy.price.dal.po.mbg;

public class CityCode {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city_code.pcode
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    private String pcode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city_code.pname
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    private String pname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city_code.plevel
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    private String plevel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city_code.parentid
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    private String parentid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city_code.province_code
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    private String provinceCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city_code.is_spot_area
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    private Byte isSpotArea;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city_code.pcode
     *
     * @return the value of city_code.pcode
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public String getPcode() {
        return pcode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city_code.pcode
     *
     * @param pcode the value for city_code.pcode
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public void setPcode(String pcode) {
        this.pcode = pcode == null ? null : pcode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city_code.pname
     *
     * @return the value of city_code.pname
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public String getPname() {
        return pname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city_code.pname
     *
     * @param pname the value for city_code.pname
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public void setPname(String pname) {
        this.pname = pname == null ? null : pname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city_code.plevel
     *
     * @return the value of city_code.plevel
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public String getPlevel() {
        return plevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city_code.plevel
     *
     * @param plevel the value for city_code.plevel
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public void setPlevel(String plevel) {
        this.plevel = plevel == null ? null : plevel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city_code.parentid
     *
     * @return the value of city_code.parentid
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public String getParentid() {
        return parentid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city_code.parentid
     *
     * @param parentid the value for city_code.parentid
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city_code.province_code
     *
     * @return the value of city_code.province_code
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public String getProvinceCode() {
        return provinceCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city_code.province_code
     *
     * @param provinceCode the value for city_code.province_code
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city_code.is_spot_area
     *
     * @return the value of city_code.is_spot_area
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public Byte getIsSpotArea() {
        return isSpotArea;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city_code.is_spot_area
     *
     * @param isSpotArea the value for city_code.is_spot_area
     *
     * @mbg.generated Sat May 07 10:44:45 CST 2022
     */
    public void setIsSpotArea(Byte isSpotArea) {
        this.isSpotArea = isSpotArea;
    }
}