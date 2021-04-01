import * as React from 'react';

import { StyleSheet, View, Text, Platform } from 'react-native';
import Dengage from 'react-native-dengage';
import { DengageTypes } from '../../src/types';

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

  React.useEffect(() => {
    if (Platform.OS === 'ios') {
      Dengage.promptForPushNotificationsWitCallback(async (hasPermission) => {
        console.log("hasPermission", hasPermission)
        Dengage.setUserPermission(hasPermission)
        setResult(String(hasPermission))
        if (hasPermission) {
          Dengage.setContactKey("Your-contact-key-here.");
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
        }
      })
    } else {
      Dengage.setContactKey("Your-contact-key-here.");
      const invokeIt = async () => {
        const hasPermissions = await Dengage.getUserPermission()
        setResult(String(hasPermissions))
        const token = await Dengage.getToken()
        setToken(token);
        setContactKey(await Dengage.getContactKey())
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

  return (
    <View style={styles.container}>
      <Text style={styles.heading}>HasPermission</Text>
      <Text>{result}</Text>
      <Text style={styles.heading}>Token</Text>
      <Text>{token}</Text>
      <Text style={styles.heading}>Contact Key</Text>
      <Text>{contactKey}</Text>

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
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
  }
});
