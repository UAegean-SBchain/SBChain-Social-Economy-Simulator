package com.example.ethereumserviceapp.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.6.1.
 */
@SuppressWarnings("rawtypes")
public class CaseMonitor extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b50612280806100206000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c80638b6e33741161008c578063d840e71711610066578063d840e717146104b2578063d871b5ad1461062f578063ea028820146106a5578063fb40c22a146106f0576100cf565b80638b6e3374146102b15780638e2aa1f314610306578063d4988c1514610459576100cf565b80630a6184c1146100d45780630d8f003b14610115578063235f4c121461016e57806345b10ce4146101af57806356066bed146102045780637f16a03414610270575b600080fd5b610113600480360360208110156100ea57600080fd5b8101908080356fffffffffffffffffffffffffffffffff1916906020019092919050505061074f565b005b6101546004803603602081101561012b57600080fd5b8101908080356fffffffffffffffffffffffffffffffff191690602001909291905050506109a7565b604051808215151515815260200191505060405180910390f35b6101ad6004803603602081101561018457600080fd5b8101908080356fffffffffffffffffffffffffffffffff19169060200190929190505050610a33565b005b6101ee600480360360208110156101c557600080fd5b8101908080356fffffffffffffffffffffffffffffffff19169060200190929190505050610fc1565b6040518082815260200191505060405180910390f35b61026e600480360360a081101561021a57600080fd5b8101908080356fffffffffffffffffffffffffffffffff19169060200190929190803560ff169060200190929190803590602001909291908035906020019092919080359060200190929190505050611004565b005b6102af6004803603602081101561028657600080fd5b8101908080356fffffffffffffffffffffffffffffffff191690602001909291905050506111d9565b005b6102f0600480360360208110156102c757600080fd5b8101908080356fffffffffffffffffffffffffffffffff1916906020019092919050505061164c565b6040518082815260200191505060405180910390f35b6103456004803603602081101561031c57600080fd5b8101908080356fffffffffffffffffffffffffffffffff1916906020019092919050505061168f565b60405180856fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001806020018060200180602001848103845287818151815260200191508051906020019060200280838360005b838110156103bc5780820151818401526020810190506103a1565b50505050905001848103835286818151815260200191508051906020019060200280838360005b838110156103fe5780820151818401526020810190506103e3565b50505050905001848103825285818151815260200191508051906020019060200280838360005b83811015610440578082015181840152602081019050610425565b5050505090500197505050505050505060405180910390f35b6104986004803603602081101561046f57600080fd5b8101908080356fffffffffffffffffffffffffffffffff19169060200190929190505050611821565b604051808215151515815260200191505060405180910390f35b6104f1600480360360208110156104c857600080fd5b8101908080356fffffffffffffffffffffffffffffffff191690602001909291905050506118ad565b60405180896fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200188815260200180602001806020018060200187600681111561053f57fe5b60ff16815260200186815260200185815260200184810384528a818151815260200191508051906020019060200280838360005b8381101561058e578082015181840152602081019050610573565b50505050905001848103835289818151815260200191508051906020019060200280838360005b838110156105d05780820151818401526020810190506105b5565b50505050905001848103825288818151815260200191508051906020019060200280838360005b838110156106125780820151818401526020810190506105f7565b505050509050019b50505050505050505050505060405180910390f35b6106a3600480360360c081101561064557600080fd5b8101908080356fffffffffffffffffffffffffffffffff1916906020019092919080359060200190929190803560ff169060200190929190803590602001909291908035906020019092919080359060200190929190505050611a71565b005b6106ee600480360360408110156106bb57600080fd5b8101908080356fffffffffffffffffffffffffffffffff1916906020019092919080359060200190929190505050611ba6565b005b6106f8611e75565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b8381101561073b578082015181840152602081019050610720565b505050509050019250505060405180910390f35b610758816109a7565b1561076257600080fd5b606060016040519080825280602002602001820160405280156107945781602001602082028038833980820191505090505b5090506000816000815181106107a657fe5b602002602001018181525050606060016040519080825280602002602001820160405280156107e45781602001602082028038833980820191505090505b5090506000816000815181106107f657fe5b602002602001018181525050606060016040519080825280602002602001820160405280156108345781602001602082028038833980820191505090505b50905060008160008151811061084657fe5b6020026020010190600681111561085957fe5b9081600681111561086657fe5b8152505060026040518060800160405280866fffffffffffffffffffffffffffffffff19168152602001858152602001848152602001838152509080600181540180825580915050906001820390600052602060002090600402016000909192909190915060008201518160000160006101000a8154816fffffffffffffffffffffffffffffffff021916908360801c02179055506020820151816001019080519060200190610917929190611f61565b506040820151816002019080519060200190610934929190611f61565b506060820151816003019080519060200190610951929190611fae565b50505050600060016002805490500390508060036000876fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020819055505050505050565b60008060028054905014156109bf5760009050610a2e565b816fffffffffffffffffffffffffffffffff191660026109de8461164c565b815481106109e857fe5b906000526020600020906004020160000160009054906101000a900460801b6fffffffffffffffffffffffffffffffff19161415610a295760019050610a2e565b600090505b919050565b610a3c81611821565b610a4557600080fd5b6000610a5082610fc1565b90506001600080549050038114610f1157610a6961205d565b60008281548110610a7657fe5b9060005260206000209060080201604051806101000160405290816000820160009054906101000a900460801b6fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020016001820154815260200160028201805480602002602001604051908101604052809291908181526020018280548015610b2657602002820191906000526020600020905b815481526020019060010190808311610b12575b5050505050815260200160038201805480602002602001604051908101604052809291908181526020018280548015610bac57602002820191906000526020600020906000905b82829054906101000a900460ff166006811115610b8657fe5b81526020019060010190602082600001049283019260010382029150808411610b6d5790505b5050505050815260200160048201805480602002602001604051908101604052809291908181526020018280548015610c0457602002820191906000526020600020905b815481526020019060010190808311610bf0575b505050505081526020016005820160009054906101000a900460ff166006811115610c2b57fe5b6006811115610c3657fe5b815260200160068201548152602001600782015481525050905060008060016000805490500381548110610c6657fe5b906000526020600020906008020160000160009054906101000a900460801b905060016000826fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002060009055600060016000805490500381548110610cd957fe5b906000526020600020906008020160008481548110610cf457fe5b90600052602060002090600802016000820160009054906101000a900460801b8160000160006101000a8154816fffffffffffffffffffffffffffffffff021916908360801c0217905550600182015481600101556002820181600201908054610d5f9291906120c0565b506003820181600301908054610d76929190612112565b506004820181600401908054610d8d9291906120c0565b506005820160009054906101000a900460ff168160050160006101000a81548160ff02191690836006811115610dbf57fe5b0217905550600682015481600601556007820154816007015590505081600060016000805490500381548110610df157fe5b906000526020600020906008020160008201518160000160006101000a8154816fffffffffffffffffffffffffffffffff021916908360801c0217905550602082015181600101556040820151816002019080519060200190610e55929190611f61565b506060820151816003019080519060200190610e72929190611fae565b506080820151816004019080519060200190610e8f929190611f61565b5060a08201518160050160006101000a81548160ff02191690836006811115610eb457fe5b021790555060c0820151816006015560e082015181600701559050508260016000836fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020019081526020016000208190555050505b6000805480610f1c57fe5b6001900381819060005260206000209060080201600080820160006101000a8154906fffffffffffffffffffffffffffffffff02191690556001820160009055600282016000610f6c9190612172565b600382016000610f7c9190612193565b600482016000610f8c9190612172565b6005820160006101000a81549060ff02191690556006820160009055600782016000905550509055610fbd826111d9565b5050565b600060016000836fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020549050919050565b61100d85611821565b61101657600080fd5b600061102186610fc1565b9050600080828154811061103157fe5b90600052602060002090600802019050600061104c8861164c565b905060006002828154811061105d57fe5b9060005260206000209060040201905060008160010160008154811061107f57fe5b906000526020600020015414156111165786816001016000815481106110a157fe5b906000526020600020018190555085816002016000815481106110c057fe5b906000526020600020018190555087816003016000815481106110df57fe5b90600052602060002090602091828204019190066101000a81548160ff0219169083600681111561110c57fe5b02179055506111c5565b80600101879080600181540180825580915050906001820390600052602060002001600090919290919091505550806002018690806001815401808255809150509060018203906000526020600020016000909192909190915055508060030188908060018154018082558091505090600182039060005260206000209060209182820401919006909192909190916101000a81548160ff021916908360068111156111be57fe5b0217905550505b848360060181905550505050505050505050565b6111e2816109a7565b6111eb57600080fd5b60006111f68261164c565b905060016002805490500381146115d15761120f6121bb565b6002828154811061121c57fe5b90600052602060002090600402016040518060800160405290816000820160009054906101000a900460801b6fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001600182018054806020026020016040519081016040528092919081815260200182805480156112c157602002820191906000526020600020905b8154815260200190600101908083116112ad575b505050505081526020016002820180548060200260200160405190810160405280929190818152602001828054801561131957602002820191906000526020600020905b815481526020019060010190808311611305575b505050505081526020016003820180548060200260200160405190810160405280929190818152602001828054801561139f57602002820191906000526020600020906000905b82829054906101000a900460ff16600681111561137957fe5b815260200190600101906020826000010492830192600103820291508084116113605790505b505050505081525050905060006002600160028054905003815481106113c157fe5b906000526020600020906004020160000160009054906101000a900460801b905060036000826fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020019081526020016000206000905560026001600280549050038154811061143457fe5b90600052602060002090600402016002848154811061144f57fe5b90600052602060002090600402016000820160009054906101000a900460801b8160000160006101000a8154816fffffffffffffffffffffffffffffffff021916908360801c021790555060018201816001019080546114b09291906120c0565b5060028201816002019080546114c79291906120c0565b5060038201816003019080546114de929190612112565b50905050816002600160028054905003815481106114f857fe5b906000526020600020906004020160008201518160000160006101000a8154816fffffffffffffffffffffffffffffffff021916908360801c02179055506020820151816001019080519060200190611552929190611f61565b50604082015181600201908051906020019061156f929190611f61565b50606082015181600301908051906020019061158c929190611fae565b509050508260036000836fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff191681526020019081526020016000208190555050505b60028054806115dc57fe5b6001900381819060005260206000209060040201600080820160006101000a8154906fffffffffffffffffffffffffffffffff02191690556001820160006116249190612172565b6002820160006116349190612172565b6003820160006116449190612193565b505090555050565b600060036000836fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff19168152602001908152602001600020549050919050565b6000606080606061169f856109a7565b6116a857600080fd5b600060026116b58761164c565b815481106116bf57fe5b906000526020600020906004020190508060000160009054906101000a900460801b8160010182600201836003018280548060200260200160405190810160405280929190818152602001828054801561173857602002820191906000526020600020905b815481526020019060010190808311611724575b505050505092508180548060200260200160405190810160405280929190818152602001828054801561178a57602002820191906000526020600020905b815481526020019060010190808311611776575b505050505091508080548060200260200160405190810160405280929190818152602001828054801561180a57602002820191906000526020600020906000905b82829054906101000a900460ff1660068111156117e457fe5b815260200190600101906020826000010492830192600103820291508084116117cb5790505b505050505090509450945094509450509193509193565b600080600080549050141561183957600090506118a8565b816fffffffffffffffffffffffffffffffff1916600061185884610fc1565b8154811061186257fe5b906000526020600020906008020160000160009054906101000a900460801b6fffffffffffffffffffffffffffffffff191614156118a357600190506118a8565b600090505b919050565b600080606080606060008060006118c389611821565b6118cc57600080fd5b6000806118d88b610fc1565b815481106118e257fe5b906000526020600020906008020190508060000160009054906101000a900460801b81600101548260020183600301846004018560050160009054906101000a900460ff16866006015487600701548580548060200260200160405190810160405280929190818152602001828054801561197c57602002820191906000526020600020905b815481526020019060010190808311611968575b50505050509550848054806020026020016040519081016040528092919081815260200182805480156119fc57602002820191906000526020600020906000905b82829054906101000a900460ff1660068111156119d657fe5b815260200190600101906020826000010492830192600103820291508084116119bd5790505b5050505050945083805480602002602001604051908101604052809291908181526020018280548015611a4e57602002820191906000526020600020905b815481526020019060010190808311611a3a575b505050505093509850985098509850985098509850985050919395975091939597565b611a7a86611821565b611a8357600080fd5b6000611a8e87610fc1565b90506000808281548110611a9e57fe5b90600052602060002090600802019050868160010181905550806002018790806001815401808255809150509060018203906000526020600020016000909192909190915055508060030186908060018154018082558091505090600182039060005260206000209060209182820401919006909192909190916101000a81548160ff02191690836006811115611b3157fe5b02179055505080600401859080600181540180825580915050906001820390600052602060002001600090919290919091505550858160050160006101000a81548160ff02191690836006811115611b8557fe5b02179055508381600601819055508281600701819055505050505050505050565b611baf82611821565b15611bb957600080fd5b60606001604051908082528060200260200182016040528015611beb5781602001602082028038833980820191505090505b5090508181600081518110611bfc57fe5b60200260200101818152505060606001604051908082528060200260200182016040528015611c3a5781602001602082028038833980820191505090505b509050600081600081518110611c4c57fe5b60200260200101906006811115611c5f57fe5b90816006811115611c6c57fe5b8152505060606001604051908082528060200260200182016040528015611ca25781602001602082028038833980820191505090505b509050600081600081518110611cb457fe5b6020026020010181815250506000604051806101000160405280876fffffffffffffffffffffffffffffffff1916815260200186815260200185815260200184815260200183815260200160006006811115611d0c57fe5b81526020016000815260200160008152509080600181540180825580915050906001820390600052602060002090600802016000909192909190915060008201518160000160006101000a8154816fffffffffffffffffffffffffffffffff021916908360801c0217905550602082015181600101556040820151816002019080519060200190611d9e929190611f61565b506060820151816003019080519060200190611dbb929190611fae565b506080820151816004019080519060200190611dd8929190611f61565b5060a08201518160050160006101000a81548160ff02191690836006811115611dfd57fe5b021790555060c0820151816006015560e08201518160070155505050600060016000805490500390508060016000886fffffffffffffffffffffffffffffffff19166fffffffffffffffffffffffffffffffff1916815260200190815260200160002081905550611e6d8661074f565b505050505050565b606080600080549050604051908082528060200260200182016040528015611eac5781602001602082028038833980820191505090505b509050600080805490501115611f5a5760008090506000808054905090505b6000811115611f575760006001820381548110611ee457fe5b906000526020600020906008020160000160009054906101000a900460801b838380600101945081518110611f1557fe5b60200260200101906fffffffffffffffffffffffffffffffff191690816fffffffffffffffffffffffffffffffff191681525050808060019003915050611ecb565b50505b8091505090565b828054828255906000526020600020908101928215611f9d579160200282015b82811115611f9c578251825591602001919060010190611f81565b5b509050611faa91906121f6565b5090565b82805482825590600052602060002090601f0160209004810192821561204c5791602002820160005b8382111561201d57835183826101000a81548160ff02191690836006811115611ffc57fe5b02179055509260200192600101602081600001049283019260010302611fd7565b801561204a5782816101000a81549060ff021916905560010160208160000104928301926001030261201d565b505b509050612059919061221b565b5090565b60405180610100016040528060006fffffffffffffffffffffffffffffffff1916815260200160008152602001606081526020016060815260200160608152602001600060068111156120ac57fe5b815260200160008152602001600081525090565b8280548282559060005260206000209081019282156121015760005260206000209182015b828111156121005782548255916001019190600101906120e5565b5b50905061210e91906121f6565b5090565b82805482825590600052602060002090601f0160209004810192821561216157600052602060002091601f016020900482015b82811115612160578254825591600101919060010190612145565b5b50905061216e919061221b565b5090565b508054600082559060005260206000209081019061219091906121f6565b50565b50805460008255601f0160209004906000526020600020908101906121b891906121f6565b50565b604051806080016040528060006fffffffffffffffffffffffffffffffff191681526020016060815260200160608152602001606081525090565b61221891905b808211156122145760008160009055506001016121fc565b5090565b90565b61224891905b8082111561224457600081816101000a81549060ff021916905550600101612221565b5090565b9056fea265627a7a723158201922b7ec4812952e7013ee4bd7da12bcb4bc542d6990fee71c00b3384aa13b3e64736f6c63430005100032";

    public static final String FUNC__GETCASEINDEX = "_getCaseIndex";

    public static final String FUNC__GETPAYMENTINDEX = "_getPaymentIndex";

    public static final String FUNC_ADDCASE = "addCase";

    public static final String FUNC_ADDCASEPAYMENT = "addCasePayment";

    public static final String FUNC_UPDATECASE = "updateCase";

    public static final String FUNC_ADDPAYMENT = "addPayment";

    public static final String FUNC_CASEEXISTS = "caseExists";

    public static final String FUNC_PAYMENTEXISTS = "paymentExists";

    public static final String FUNC_GETALLCASES = "getAllCases";

    public static final String FUNC_GETCASE = "getCase";

    public static final String FUNC_GETPAYMENT = "getPayment";

    public static final String FUNC_DELETECASE = "deleteCase";

    public static final String FUNC_DELETEPAYMENT = "deletePayment";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("3", "0xE6311dc716d7e344FDE4ce9A1Fbdb7766E4C82b6");
    }

    @Deprecated
    protected CaseMonitor(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CaseMonitor(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CaseMonitor(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CaseMonitor(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> _getCaseIndex(byte[] _uuid) {
        final Function function = new Function(FUNC__GETCASEINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> _getPaymentIndex(byte[] _uuid) {
        final Function function = new Function(FUNC__GETPAYMENTINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> addCase(byte[] _uuid, BigInteger _date) {
        final Function function = new Function(
                FUNC_ADDCASE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid), 
                new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addCasePayment(byte[] _uuid) {
        final Function function = new Function(
                FUNC_ADDCASEPAYMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateCase(byte[] _uuid, BigInteger _date, BigInteger _state, BigInteger _payPerDay, BigInteger _offset, BigInteger _rejectionDate) {
        final Function function = new Function(
                FUNC_UPDATECASE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid), 
                new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint8(_state), 
                new org.web3j.abi.datatypes.generated.Uint256(_payPerDay), 
                new org.web3j.abi.datatypes.generated.Uint256(_offset), 
                new org.web3j.abi.datatypes.generated.Uint256(_rejectionDate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addPayment(byte[] _uuid, BigInteger _state, BigInteger _pDate, BigInteger _payHistory, BigInteger _offset) {
        final Function function = new Function(
                FUNC_ADDPAYMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid), 
                new org.web3j.abi.datatypes.generated.Uint8(_state), 
                new org.web3j.abi.datatypes.generated.Uint256(_pDate), 
                new org.web3j.abi.datatypes.generated.Uint256(_payHistory), 
                new org.web3j.abi.datatypes.generated.Uint256(_offset)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> caseExists(byte[] _uuid) {
        final Function function = new Function(FUNC_CASEEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> paymentExists(byte[] _uuid) {
        final Function function = new Function(FUNC_PAYMENTEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<List> getAllCases() {
        final Function function = new Function(FUNC_GETALLCASES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes16>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<Tuple8<byte[], BigInteger, List<BigInteger>, List<BigInteger>, List<BigInteger>, BigInteger, BigInteger, BigInteger>> getCase(byte[] _uuid) {
        final Function function = new Function(FUNC_GETCASE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes16>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint8>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple8<byte[], BigInteger, List<BigInteger>, List<BigInteger>, List<BigInteger>, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple8<byte[], BigInteger, List<BigInteger>, List<BigInteger>, List<BigInteger>, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple8<byte[], BigInteger, List<BigInteger>, List<BigInteger>, List<BigInteger>, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple8<byte[], BigInteger, List<BigInteger>, List<BigInteger>, List<BigInteger>, BigInteger, BigInteger, BigInteger>(
                                (byte[]) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                convertToNative((List<Uint256>) results.get(2).getValue()), 
                                convertToNative((List<Uint8>) results.get(3).getValue()), 
                                convertToNative((List<Uint256>) results.get(4).getValue()), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (BigInteger) results.get(7).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple4<byte[], List<BigInteger>, List<BigInteger>, List<BigInteger>>> getPayment(byte[] _uuid) {
        final Function function = new Function(FUNC_GETPAYMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes16>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint8>>() {}));
        return new RemoteFunctionCall<Tuple4<byte[], List<BigInteger>, List<BigInteger>, List<BigInteger>>>(function,
                new Callable<Tuple4<byte[], List<BigInteger>, List<BigInteger>, List<BigInteger>>>() {
                    @Override
                    public Tuple4<byte[], List<BigInteger>, List<BigInteger>, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<byte[], List<BigInteger>, List<BigInteger>, List<BigInteger>>(
                                (byte[]) results.get(0).getValue(), 
                                convertToNative((List<Uint256>) results.get(1).getValue()), 
                                convertToNative((List<Uint256>) results.get(2).getValue()), 
                                convertToNative((List<Uint8>) results.get(3).getValue()));
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> deleteCase(byte[] _uuid) {
        final Function function = new Function(
                FUNC_DELETECASE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deletePayment(byte[] _uuid) {
        final Function function = new Function(
                FUNC_DELETEPAYMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes16(_uuid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static CaseMonitor load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CaseMonitor(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CaseMonitor load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CaseMonitor(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CaseMonitor load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new CaseMonitor(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CaseMonitor load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CaseMonitor(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CaseMonitor> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CaseMonitor.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<CaseMonitor> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CaseMonitor.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<CaseMonitor> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CaseMonitor.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<CaseMonitor> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CaseMonitor.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
