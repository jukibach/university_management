package fpt.com.universitymanagement.mapper;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.entity.account.RoleAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface AccountMapper {
    default Set<String> mapRoleAccounts(Set<RoleAccount> roleAccounts) {
        return roleAccounts.stream()
                .map(roleAccount -> roleAccount.getRole().getName())
                .collect(Collectors.toSet());
    }
    
    @Mapping(target = "roleAccounts", source = "roleAccounts")
    AccountResponse accountToAccountResponse(Account account);
}
