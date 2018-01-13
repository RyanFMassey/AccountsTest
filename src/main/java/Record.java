import java.math.BigDecimal;
import java.util.Date;

public class Record {
	private Date date;
	private BigDecimal card;
	private BigDecimal delivery;
	private BigDecimal cash;
	
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public BigDecimal getCard() {
		return card;
	}
	
	public void setCard(BigDecimal card) {
		this.card = card;
	}
	
	public BigDecimal getDelivery() {
		return delivery;
	}
	
	public void setDelivery(BigDecimal delivery) {
		this.delivery = delivery;
	}
	
	public BigDecimal getCash() {
		return cash;
	}
	
	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
	
}
