package org.androidtown.skkuruit;

/**
 * Created by genie on 2017. 6. 10..
 */
public class company {
    private int companyNo;
    private String companyName;
    private String companyCate;
    private int originCompanyNo;
    private String mustQual;
    private String optionQual;


    public company() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public company(int companyNo, String companyName, String companyCate, int originCompanyNo, String mustQual, String optionQual) {
        this.companyNo = companyNo;
        this.companyName = companyName;
        this.companyCate = companyCate;
        this.originCompanyNo = originCompanyNo;
        this.mustQual = mustQual;
        this.optionQual = optionQual;
    }

    public int getCompanyNo() {
        return companyNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyCate() {
        return companyCate;
    }

    public int getOriginCompanyNo() {
        return originCompanyNo;
    }

    public String getMustQual() {
        return mustQual;
    }

    public String getOptionQual() {
        return optionQual;
    }
}
