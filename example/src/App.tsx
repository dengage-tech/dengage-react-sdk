import * as React from 'react';

import { StyleSheet, View, Text, Platform, Button, TextInput } from 'react-native';
import Dengage from 'react-native-dengage';
import { DengageTypes } from '../../src/types';
import { useFocusEffect, useNavigation } from '@react-navigation/native';
import { BaseButton } from 'react-native-gesture-handler';

export default function App() {
  const [result, setResult] = React.useState<string>('checking...');
  const [token, setToken] = React.useState<string>('checking...');
  const [contactKey, setContactKey] = React.useState<string>('checking...');

  const pageViewExample = () => {
    // pageView event example
    Dengage.pageView({
      "product_id":1,
      "product_variant_id":1,
      "quantity":1,
      "unit_price":10.00,
      "discounted_price":9.99,
    })
  }
  const addToCartExample = () => {
    // addToCart action starts here.
    // All items currently exists in shopping cart must be added to an array
    const cartItem = {}
    cartItem["product_id"] = 1
    cartItem["product_variant_id"] = 1
    cartItem["quantity"] = 1
    cartItem["unit_price"] = 10.00
    cartItem["discounted_price"] = 9.99
    // ... extra columns in shopping_cart_events_detail table, can be added in cartItem

    let cartItems = []
    cartItems.push(cartItem)
    cartItems.push(cartItem)


    // Add to cart action
    const addParams = {
      "product_id":1,
      "product_variant_id":1,
      "quantity":1,
      "unit_price":10.00,
      "discounted_price":9.99,
      // ... extra columns in shopping_cart_events table, can be added here
      "cartItems":cartItems // all items in cart
    }
    Dengage.addToCart(addParams)
    // addToCart action ends here.
  }
  const removeFromCartExample = () => {
    const cartItem = {}
    cartItem["product_id"] = 1
    cartItem["product_variant_id"] = 1
    cartItem["quantity"] = 1
    cartItem["unit_price"] = 10.00
    cartItem["discounted_price"] = 9.99
    // ... extra columns in shopping_cart_events_detail table, can be added in cartItem

    let cartItems = []
    cartItems.push(cartItem)
    cartItems.push(cartItem)

    // Remove from cart action
    const removeParams = {
      "product_id":1,
      "product_variant_id":1,
      "quantity":1,
      "unit_price":10.00,
      "discounted_price":9.99,
      // ... extra columns in shopping_cart_events table, can be added here
      "cartItems":cartItems // all items in cart
    }
    Dengage.removeFromCart(removeParams)
  }
  const viewCart = () => {
    const cartItem = {}
    cartItem["product_id"] = 1
    cartItem["product_variant_id"] = 1
    cartItem["quantity"] = 1
    cartItem["unit_price"] = 10.00
    cartItem["discounted_price"] = 9.99
    // ... extra columns in shopping_cart_events_detail table, can be added in cartItem

    let cartItems = []
    cartItems.push(cartItem)
    cartItems.push(cartItem)
    Dengage.viewCart(cartItems)
  }

  const appInboxExamples = async () => {
    const inboxMessages = await Dengage.getInboxMessages(10, 20)
      .catch(err => err)
    console.log("inboxMessages", inboxMessages)

    const delResponse = await Dengage.deleteInboxMessage("your-message-id-here")
      .catch(res => res)
    console.log("deleteInboxMessage: ", delResponse);

    const messageSetAsInboxRes = await Dengage.setInboxMessageAsClicked("your-message-id-here")
      .catch(res => res)
    console.log("messageSetAsInboxRes: ", messageSetAsInboxRes);

  }

  React.useEffect(() => {
    if (Platform.OS === 'ios') {
      Dengage.promptForPushNotificationsWitCallback(async (hasPermission) => {
        console.log("hasPermission", hasPermission)
        Dengage.setUserPermission(hasPermission)
        setResult(String(hasPermission))
        if (hasPermission) {
          const token = await Dengage.getToken()
          console.log("tokenIs: ", token)
          setToken(token)
          if (token) {
            Dengage.setToken(token)
            Dengage.setLogStatus(true);
          }
          setContactKey(await Dengage.getContactKey())

          pageViewExample()
          addToCartExample()
          removeFromCartExample()
          appInboxExamples()
        }
      })
    } else {
      const invokeIt = async () => {
        const hasPermissions = await Dengage.getUserPermission()
        setResult(String(hasPermissions))
        const token = await Dengage.getToken()
        setToken(token);
        setContactKey((await Dengage.getContactKey()) ?? "")
        const subscription = await Dengage.getSubscription()
        console.log("subscription", subscription)
        pageViewExample()
        addToCartExample()
        // removeFromCartExample()
        appInboxExamples()
      }
      invokeIt()
    }


    if (Platform.OS === 'ios') {
      Dengage.handleNotificationActionBlock((notificationAction: DengageTypes["NotificationAction"]) => {
        console.log(notificationAction.notification)
        // handle notification {notificationAction} here.
      })
    }

  }, []);

  const navigation = useNavigation()
  useFocusEffect(
    React.useCallback(() => {
      Dengage.setNavigation();
    }, [])
  );

  return (
    <View style={styles.container}>
      <Text style={styles.heading}>HasPermission</Text>
      <Text>{result}</Text>
      <Text style={styles.heading}>Token</Text>
      <Text>{token}</Text>

      <Text>Enter Contact Key:</Text>
      <TextInput
        value={contactKey}
        onChangeText={(val) => setContactKey(val)}
        style={styles.input}/>

      <View style={styles.btnContainer}>
        <Button
          onPress={async () => {
            await Dengage.setContactKey(contactKey)
          }}
          title={"update Contact Key"}
        />
      </View>

      <View style={styles.btnContainer}>
        <Button
          onPress={async () => {
            alert("contactKey: " + await Dengage.getContactKey())
          }}
          title={"show Contact Key"}
        />
      </View>

      <View style={styles.btnContainer}>
        <Button
          onPress={() => navigation.navigate('SecondScreen')}
          title={"go to second screen"}
        />
      </View>
      <Text>Note: On Navigation to second screen, setNavigationWithName is called.</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  heading: {
    fontWeight: 'bold',
    marginTop: 20
  },
  input: {
    borderWidth: 1,
    width: 250,
    borderColor: 'black'
  },
  btnContainer: {
    margin: 10
  }
});
