package com.github.east196.fireworks.web.datatables;

import java.util.Objects;

public class DataTableResult {
	private int draw;
	private long recordsTotal;
	private long recordsFiltered;
	private Object data;

	public DataTableResult() {
	}

	public DataTableResult(int draw, long recordsTotal, int recordsFiltered, Object data) {
		this.draw = draw;
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
		this.data = data;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DataTableResult that = (DataTableResult) o;
		return draw == that.draw &&
				recordsTotal == that.recordsTotal &&
				recordsFiltered == that.recordsFiltered &&
				Objects.equals(data, that.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(draw, recordsTotal, recordsFiltered, data);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("DataTableResult{");
		sb.append("draw=").append(draw);
		sb.append(", recordsTotal=").append(recordsTotal);
		sb.append(", recordsFiltered=").append(recordsFiltered);
		sb.append(", data=").append(data);
		sb.append('}');
		return sb.toString();
	}
}