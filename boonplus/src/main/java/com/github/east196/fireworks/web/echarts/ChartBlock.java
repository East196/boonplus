package com.github.east196.fireworks.web.echarts;

import java.util.Objects;

public class ChartBlock {

  private String name;

  private Integer value;

  public ChartBlock() {
  }

  public ChartBlock(String name, Integer value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ChartBlock)) return false;
    ChartBlock that = (ChartBlock) o;
    return Objects.equals(name, that.name) &&
            Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ChartBlock{");
    sb.append("name='").append(name).append('\'');
    sb.append(", value=").append(value);
    sb.append('}');
    return sb.toString();
  }
}