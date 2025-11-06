import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
  Alert,
} from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import Dengage, { type Cart, type CartItem } from '@dengage-tech/react-native-dengage';

const CartScreen: React.FC = () => {
  const [cartItems, setCartItems] = useState<CartItem[]>([]);

  useEffect(() => {
    loadCart();
  }, []);

  const loadCart = async () => {
    try {
      const cart = await Dengage.getCart();
      setCartItems(cart.items || []);
    } catch (error) {
      Alert.alert('Error', 'Failed to load cart');
      console.error('Error loading cart:', error);
    }
  };

  const handleAddItem = () => {
    const newItem: CartItem = {
      productId: '',
      productVariantId: '',
      categoryPath: '',
      price: 0,
      discountedPrice: 0,
      hasDiscount: false,
      hasPromotion: false,
      quantity: 1,
      attributes: {},
      effectivePrice: 0,
      lineTotal: 0,
      discountedLineTotal: 0,
      effectiveLineTotal: 0,
      categorySegments: [],
      categoryRoot: '',
    };
    setCartItems([...cartItems, newItem]);
  };

  const handleRemoveItem = (index: number) => {
    if (index >= 0 && index < cartItems.length) {
      const updated = cartItems.filter((_, i) => i !== index);
      setCartItems(updated);
    }
  };

  const handleItemChange = (
    index: number,
    field: keyof CartItem,
    value: string | number | boolean
  ) => {
    const updated = [...cartItems];
    updated[index] = { ...updated[index], [field]: value };
    setCartItems(updated);
  };

  const handleUpdateCart = async () => {
    try {
      const cart: Cart = {
        items: cartItems,
        summary: {
          currency: 'TRY',
          updatedAt: Date.now(),
          linesCount: cartItems.length,
          itemsCount: cartItems.reduce((sum, item) => sum + item.quantity, 0),
          subtotal: 0,
          discountedSubtotal: 0,
          effectiveSubtotal: 0,
          anyDiscounted: false,
          allDiscounted: false,
          minPrice: 0,
          maxPrice: 0,
          minEffectivePrice: 0,
          maxEffectivePrice: 0,
          categories: {},
        },
      };

      await Dengage.setCart(cart);
      Alert.alert('Success', 'Cart updated successfully!');
    } catch (error) {
      Alert.alert('Error', 'Failed to update cart');
      console.error('Error updating cart:', error);
    }
  };

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
      <ScrollView contentContainerStyle={styles.container}>
        <TouchableOpacity style={styles.addButton} onPress={handleAddItem}>
          <Text style={styles.addButtonText}>+ Add Item</Text>
        </TouchableOpacity>

        {cartItems.map((item, index) => (
          <View key={index} style={styles.itemCard}>
            <Text style={styles.itemTitle}>Item {index + 1}</Text>

            <NoCapTextInput
              style={styles.input}
              placeholder="Product ID"
              value={item.productId}
              onChangeText={(text) => handleItemChange(index, 'productId', text)}
            />

            <NoCapTextInput
              style={styles.input}
              placeholder="Product Variant ID"
              value={item.productVariantId}
              onChangeText={(text) =>
                handleItemChange(index, 'productVariantId', text)
              }
            />

            <NoCapTextInput
              style={styles.input}
              placeholder="Category Path"
              value={item.categoryPath}
              onChangeText={(text) =>
                handleItemChange(index, 'categoryPath', text)
              }
            />

            <NoCapTextInput
              style={styles.input}
              placeholder="Price"
              value={item.price.toString()}
              onChangeText={(text) =>
                handleItemChange(index, 'price', parseInt(text) || 0)
              }
              keyboardType="numeric"
            />

            <NoCapTextInput
              style={styles.input}
              placeholder="Discounted Price"
              value={item.discountedPrice.toString()}
              onChangeText={(text) =>
                handleItemChange(index, 'discountedPrice', parseInt(text) || 0)
              }
              keyboardType="numeric"
            />

            <NoCapTextInput
              style={styles.input}
              placeholder="Quantity"
              value={item.quantity.toString()}
              onChangeText={(text) =>
                handleItemChange(index, 'quantity', parseInt(text) || 1)
              }
              keyboardType="numeric"
            />

            <View style={styles.checkboxRow}>
              <TouchableOpacity
                style={styles.checkbox}
                onPress={() =>
                  handleItemChange(index, 'hasDiscount', !item.hasDiscount)
                }>
                <Text>
                  {item.hasDiscount ? '✓' : '○'} Has Discount
                </Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.checkbox}
                onPress={() =>
                  handleItemChange(index, 'hasPromotion', !item.hasPromotion)
                }>
                <Text>
                  {item.hasPromotion ? '✓' : '○'} Has Promotion
                </Text>
              </TouchableOpacity>
            </View>

            <TouchableOpacity
              style={styles.removeButton}
              onPress={() => handleRemoveItem(index)}>
              <Text style={styles.removeButtonText}>Remove Item</Text>
            </TouchableOpacity>
          </View>
        ))}

        {cartItems.length > 0 && (
          <TouchableOpacity style={styles.updateButton} onPress={handleUpdateCart}>
            <Text style={styles.updateButtonText}>Update Cart</Text>
          </TouchableOpacity>
        )}

        {cartItems.length === 0 && (
          <Text style={styles.emptyText}>No items in cart</Text>
        )}
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 16,
    backgroundColor: '#fff',
    flexGrow: 1,
  },
  addButton: {
    marginBottom: 16,
    alignSelf: 'flex-end',
  },
  addButtonText: {
    color: '#007bff',
    fontWeight: 'bold',
    fontSize: 15,
  },
  itemCard: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 16,
    marginBottom: 16,
    backgroundColor: '#f9f9f9',
  },
  itemTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 12,
  },
  input: {
    borderWidth: 1,
    borderColor: '#bbb',
    borderRadius: 6,
    padding: 8,
    marginBottom: 10,
    backgroundColor: '#fff',
    color: '#222',
  },
  checkboxRow: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginBottom: 12,
  },
  checkbox: {
    padding: 8,
  },
  removeButton: {
    backgroundColor: '#dc3545',
    padding: 12,
    borderRadius: 6,
    alignItems: 'center',
    marginTop: 8,
  },
  removeButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  updateButton: {
    backgroundColor: '#007bff',
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 16,
  },
  updateButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  emptyText: {
    fontSize: 16,
    color: '#666',
    textAlign: 'center',
    marginTop: 32,
  },
});

export default CartScreen;

