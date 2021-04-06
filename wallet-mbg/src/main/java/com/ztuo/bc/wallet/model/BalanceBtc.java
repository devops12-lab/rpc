package com.ztuo.bc.wallet.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_balance_btc
 */
@ApiModel(value="com.ztuo.bc.wallet.model.BalanceBtc")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceBtc implements Cloneable, Serializable {
    /**
     * Database Column Remarks:
     *   账户id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_balance_btc.address
     */
    @ApiModelProperty(value="address账户id")
    private String address;

    /**
     * Database Column Remarks:
     *   币种
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_balance_btc.currency
     */
    @ApiModelProperty(value="currency币种")
    private String currency;

    /**
     * Database Column Remarks:
     *   余额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_balance_btc.amount
     */
    @ApiModelProperty(value="amount余额")
    private BigDecimal amount;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_balance_btc.update_time
     */
    @ApiModelProperty(value="updateTime更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", address=").append(address);
        sb.append(", currency=").append(currency);
        sb.append(", amount=").append(amount);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public BalanceBtc clone() throws CloneNotSupportedException {
        return (BalanceBtc) super.clone();
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table t_balance_btc
     */
    public enum Column {
        address("address", "address", "VARCHAR", false),
        currency("currency", "currency", "VARCHAR", false),
        amount("amount", "amount", "DECIMAL", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false);

        private static final String BEGINNING_DELIMITER = "`";

        private static final String ENDING_DELIMITER = "`";

        private final String column;

        private final boolean isColumnNameDelimited;

        private final String javaProperty;

        private final String jdbcType;

        public String value() {
            return this.column;
        }

        public String getValue() {
            return this.column;
        }

        public String getJavaProperty() {
            return this.javaProperty;
        }

        public String getJdbcType() {
            return this.jdbcType;
        }

        Column(String column, String javaProperty, String jdbcType, boolean isColumnNameDelimited) {
            this.column = column;
            this.javaProperty = javaProperty;
            this.jdbcType = jdbcType;
            this.isColumnNameDelimited = isColumnNameDelimited;
        }

        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        public static Column[] excludes(Column ... excludes) {
            ArrayList<Column> columns = new ArrayList<>(Arrays.asList(Column.values()));
            if (excludes != null && excludes.length > 0) {
                columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));
            }
            return columns.toArray(new Column[]{});
        }

        public static Column[] all() {
            return Column.values();
        }

        public String getEscapedColumnName() {
            if (this.isColumnNameDelimited) {
                return new StringBuilder().append(BEGINNING_DELIMITER).append(this.column).append(ENDING_DELIMITER).toString();
            } else {
                return this.column;
            }
        }

        public String getAliasedEscapedColumnName() {
            return this.getEscapedColumnName();
        }
    }
}