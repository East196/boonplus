package com.github.east196.fireworks.web.baidumap;

import java.util.Objects;

public class HotPoint extends Point{

  private Integer count;


  public HotPoint(Double lat, Double lng, Integer count) {
    super(lat,lng);
    this.count = count;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HotPoint)) return false;
    if (!super.equals(o)) return false;
    HotPoint hotPoint = (HotPoint) o;
    return Objects.equals(count, hotPoint.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), count);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("HotPoint{");
    sb.append("lat=").append(lat);
    sb.append(", count=").append(count);
    sb.append(", lng=").append(lng);
    sb.append('}');
    return sb.toString();
  }
}