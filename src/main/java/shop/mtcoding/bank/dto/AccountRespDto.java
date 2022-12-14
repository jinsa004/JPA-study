package shop.mtcoding.bank.dto;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;

public class AccountRespDto {
    @Getter
    @Setter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private String ownerName;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.ownerName = account.getOwnerName();
            this.balance = account.getBalance();
        }
    }
}
