package com.porter_replica.booking_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="booking_pricing")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookingPricing extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pricing_id")
	private Long pricingId;

	@Column(name="booking_id")
	private Long bookingId;

	@Column(name="base_fare")
	private Double baseFare;

	@Column(name="time_fare")
	private Double timeFare;

	@Column(name="surge_amount")
	private Double surgeAmount;

	@Column(name="platform_fee")
	private Double platformFee;

	@Column(name="cgst_rate")
	private Double cgstRate;

	@Column(name="cgst_amount")
	private Double cgstAmount;

	@Column(name="sgst_rate")
	private Double sgstRate;

	@Column(name="sgst_amount")
	private Double sgstAmount;

	@Column(name="discount")
	private Double discout;

	@Column(name="final_price")
	private Double finalPrice;

	@Column(name="currency")
	private String currency;

}
