package com.zero2ipo.common.entity;

/**
 * Created by zhengyunfei on 2015/8/31.
 */
public class Car {
    private int id;
    private String userCarId;
    private String carNo;
    private String carColor;
    private String carType;
    private String carSeats;
    private String washAddr;
    private String washInfo;
    private String createTime;
    private String preTime;
    private String mobile;
    private String status;
    private Address address;
    private String name;
    private String lat;
    private String lng;
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMobile() {
		return mobile==null?"":mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPreTime() {
		return preTime;
	}

	public void setPreTime(String preTime) {
		this.preTime = preTime;
	}

	public String getUserCarId() {
        return userCarId;
    }

    public void setUserCarId(String userCarId) {
        this.userCarId = userCarId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarSeats() {
        return carSeats;
    }

    public void setCarSeats(String carSeats) {
        this.carSeats = carSeats;
    }

    public String getWashAddr() {
        return washAddr;
    }

    public void setWashAddr(String washAddr) {
        this.washAddr = washAddr;
    }

    public String getWashInfo() {
        return washInfo;
    }

    public void setWashInfo(String washInfo) {
        this.washInfo = washInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
