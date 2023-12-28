package fpt.com.universitymanagement.mapper;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.entity.account.RoleAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Named("mapRoleAccounts")
    default Set<String> mapRoleAccounts(Set<RoleAccount> roleAccounts) {
        return roleAccounts.stream()
                .map(roleAccount -> roleAccount.getRole().getName())
                .collect(Collectors.toSet());
    }
    
    @Mapping(target = "roleAccounts", qualifiedByName = "mapRoleAccounts")
    AccountResponse accountToAccountResponse(Account account);
}
