package com.stripe.android.net;

import com.stripe.android.model.BankAccount;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test class for {@link TokenParser}.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23)
public class TokenParserTest {

    private static final String RAW_TOKEN = "{\n" +
            "  \"id\": \"tok_189fi32eZvKYlo2Ct0KZvU5Y\",\n" +
            "  \"object\": \"token\",\n" +
            "  \"card\": {\n" +
            "    \"id\": \"card_189fi32eZvKYlo2CHK8NPRME\",\n" +
            "    \"object\": \"card\",\n" +
            "    \"address_city\": null,\n" +
            "    \"address_country\": null,\n" +
            "    \"address_line1\": null,\n" +
            "    \"address_line1_check\": null,\n" +
            "    \"address_line2\": null,\n" +
            "    \"address_state\": null,\n" +
            "    \"address_zip\": null,\n" +
            "    \"address_zip_check\": null,\n" +
            "    \"brand\": \"Visa\",\n" +
            "    \"country\": \"US\",\n" +
            "    \"cvc_check\": null,\n" +
            "    \"dynamic_last4\": null,\n" +
            "    \"exp_month\": 8,\n" +
            "    \"exp_year\": 2017,\n" +
            "    \"funding\": \"credit\",\n" +
            "    \"last4\": \"4242\",\n" +
            "    \"metadata\": {\n" +
            "    },\n" +
            "    \"name\": null,\n" +
            "    \"tokenization_method\": null\n" +
            "  },\n" +
            "  \"client_ip\": null,\n" +
            "  \"created\": 1462905355,\n" +
            "  \"livemode\": false,\n" +
            "  \"type\": \"card\",\n" +
            "  \"used\": false\n" +
            "}";

    private static final String RAW_TOKEN_NO_ID = "{\n" +
            "  \"object\": \"token\",\n" +
            "  \"card\": {\n" +
            "    \"id\": \"card_189fi32eZvKYlo2CHK8NPRME\",\n" +
            "    \"object\": \"card\",\n" +
            "    \"address_city\": null,\n" +
            "    \"address_country\": null,\n" +
            "    \"address_line1\": null,\n" +
            "    \"address_line1_check\": null,\n" +
            "    \"address_line2\": null,\n" +
            "    \"address_state\": null,\n" +
            "    \"address_zip\": null,\n" +
            "    \"address_zip_check\": null,\n" +
            "    \"brand\": \"Visa\",\n" +
            "    \"country\": \"US\",\n" +
            "    \"cvc_check\": null,\n" +
            "    \"dynamic_last4\": null,\n" +
            "    \"exp_month\": 8,\n" +
            "    \"exp_year\": 2017,\n" +
            "    \"funding\": \"credit\",\n" +
            "    \"last4\": \"4242\",\n" +
            "    \"metadata\": {\n" +
            "    },\n" +
            "    \"name\": null,\n" +
            "    \"tokenization_method\": null\n" +
            "  },\n" +
            "  \"client_ip\": null,\n" +
            "  \"created\": 1462905355,\n" +
            "  \"livemode\": false,\n" +
            "  \"type\": \"card\",\n" +
            "  \"used\": false\n" +
            "}";

    private static final String RAW_BANK_TOKEN = "{\n" +
            "  \"id\": \"btok_9xJAbronBnS9bH\",\n" +
            "  \"object\": \"token\",\n" +
            "  \"bank_account\": {\n" +
            "    \"id\": \"ba_19dOY72eZvKYlo2CVNPhmtv3\",\n" +
            "    \"object\": \"bank_account\",\n" +
            "    \"account_holder_name\": \"Jane Austen\",\n" +
            "    \"account_holder_type\": \"individual\",\n" +
            "    \"bank_name\": \"STRIPE TEST BANK\",\n" +
            "    \"country\": \"US\",\n" +
            "    \"currency\": \"usd\",\n" +
            "    \"fingerprint\": \"1JWtPxqbdX5Gamtc\",\n" +
            "    \"last4\": \"6789\",\n" +
            "    \"routing_number\": \"110000000\",\n" +
            "    \"status\": \"new\"\n" +
            "  },\n" +
            "  \"client_ip\": null,\n" +
            "  \"created\": 1484765567,\n" +
            "  \"livemode\": false,\n" +
            "  \"type\": \"bank_account\",\n" +
            "  \"used\": false\n" +
            "}";

    private static final String RAW_BANK_TOKEN_NO_TYPE = "{\n" +
            "  \"id\": \"btok_9xJAbronBnS9bH\",\n" +
            "  \"object\": \"token\",\n" +
            "  \"bank_account\": {\n" +
            "    \"id\": \"ba_19dOY72eZvKYlo2CVNPhmtv3\",\n" +
            "    \"object\": \"bank_account\",\n" +
            "    \"account_holder_name\": \"Jane Austen\",\n" +
            "    \"account_holder_type\": \"individual\",\n" +
            "    \"bank_name\": \"STRIPE TEST BANK\",\n" +
            "    \"country\": \"US\",\n" +
            "    \"currency\": \"usd\",\n" +
            "    \"fingerprint\": \"1JWtPxqbdX5Gamtc\",\n" +
            "    \"last4\": \"6789\",\n" +
            "    \"routing_number\": \"110000000\",\n" +
            "    \"status\": \"new\"\n" +
            "  },\n" +
            "  \"client_ip\": null,\n" +
            "  \"created\": 1484765567,\n" +
            "  \"livemode\": false,\n" +
            "  \"used\": false\n" +
            "}";

    @Test
    public void parseToken_readsObject() {
        Date createdDate = new Date(1462905355L * 1000L);
        Token partialExpectedToken = new Token(
                "tok_189fi32eZvKYlo2Ct0KZvU5Y",
                false,
                createdDate,
                false,
                (Card) null);
        try {
            Token answerToken = TokenParser.parseToken(RAW_TOKEN);
            assertEquals(partialExpectedToken.getId(), answerToken.getId());
            assertEquals(partialExpectedToken.getLivemode(), answerToken.getLivemode());
            assertEquals(partialExpectedToken.getCreated(), answerToken.getCreated());
            assertEquals(partialExpectedToken.getUsed(), answerToken.getUsed());
            assertEquals(Token.TYPE_CARD, answerToken.getType());

            // Note: we test the validity of the card object in CardParserTest
            assertNotNull(answerToken.getCard());
            assertNull(answerToken.getBankAccount());
        } catch (JSONException jex) {
            fail("Json Parsing failure");
        }
    }

    @Test
    public void parseToken_whenBankAccount_readsObject() {
        Date createdDate = new Date(1484765567L * 1000L);
        Token expectedToken = new Token(
                "btok_9xJAbronBnS9bH",
                false,
                createdDate,
                false,
                (BankAccount) null);
        try {
            Token answerToken = TokenParser.parseToken(RAW_BANK_TOKEN);
            assertEquals(expectedToken.getId(), answerToken.getId());
            assertEquals(expectedToken.getLivemode(), answerToken.getLivemode());
            assertEquals(expectedToken.getCreated(), answerToken.getCreated());
            assertEquals(expectedToken.getUsed(), answerToken.getUsed());
            assertEquals(Token.TYPE_BANK_ACCOUNT, answerToken.getType());

            assertNotNull(answerToken.getBankAccount());
            assertNull(answerToken.getCard());
        } catch (JSONException jex) {
            fail("Json Parsing Failure");
        }
    }

    @Test(expected = JSONException.class)
    public void parseToken_withoutId_throwsException() throws JSONException {
        TokenParser.parseToken(RAW_TOKEN_NO_ID);
        fail("Expected an exception when parsing token without ID.");
    }

    @Test(expected = JSONException.class)
    public void parseToken_withoutType_throwsException() throws JSONException {
        TokenParser.parseToken(RAW_BANK_TOKEN_NO_TYPE);
        fail("Expected an exception when parsing token without type");
    }
 }
