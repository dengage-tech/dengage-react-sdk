import React, { useEffect, useState, useRef, useMemo } from 'react';
import {
  SafeAreaView,
  View,
  TextInput,
  Button,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
} from 'react-native';
import { StoriesListView } from '@dengage-tech/react-native-dengage';

export default function AppStoryScreen() {
  const [propertyId, setPropertyId] = useState<string>('3');
  const [screenName, setScreenName] = useState<string>('ego');
  const [storyBackgroundColor, setStoryBackgroundColor] =
    useState<string>('#ffffff');
  const [showInline, setShowInline] = useState(false);

  const storyContainerRef = useRef<View>(null);

  // On mount: send page view and set category
  useEffect(() => {}, []);

  const changeStoryBackgroundColor = () => {
    if (storyContainerRef.current) {
      // Directly update view backgroundColor
      storyContainerRef.current.setNativeProps({
        style: { backgroundColor: storyBackgroundColor },
      });
    }
  };

  const contentComponent = useMemo(() => {
    return (
      <StoriesListView
        storyPropertyId={'5'}
        screenName={'5'}
        style={styles.flex}
      />
    );
  }, []); // Add dependencies if needed

  const getContentComponent = () => {
    const a = (
      <StoriesListView
        storyPropertyId={'5'}
        screenName={'5'}
        style={styles.flex}
      />
    );

    console.log('Counter:', counter);
    if (counter === 0) {
      counter++;
      return a;
    } else {
      console.log('Counter:', counter);
      return
    }

  };

  var counter = 0;

  const refreshStory = () => {
    // Remove existing story display
    // Show new App Story
    /* Dengage.showAppStory?.({
      storyPropertyID: propertyId.trim(),
      screenName: screenName.trim() || null,
      customParams: {},
      container: storyContainerRef.current,
    }); */
    setShowInline(true);
  };

  return (
    <SafeAreaView style={styles.flex}>
      <KeyboardAvoidingView
        style={styles.flex}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      >
        <ScrollView contentContainerStyle={styles.container}>
          <TextInput
            style={styles.input}
            placeholder="Property Id"
            value={propertyId}
            onChangeText={setPropertyId}
            autoCapitalize="none"
          />

          <TextInput
            style={styles.input}
            placeholder="Screen Name"
            value={screenName}
            onChangeText={setScreenName}
            autoCapitalize="none"
          />

          <TextInput
            style={styles.input}
            placeholder="Story Background Color"
            value={storyBackgroundColor}
            onChangeText={setStoryBackgroundColor}
            autoCapitalize="none"
          />

          <Button
            title="Change Story Background Color"
            onPress={changeStoryBackgroundColor}
          />

          <View style={styles.buttonMargin}>
            <Button title="Refresh Story" onPress={refreshStory} />
          </View>

          <View
            ref={storyContainerRef}
            style={[
              styles.storyContainer,
              { backgroundColor: storyBackgroundColor },
            ]}
          >
            {showInline && propertyId.trim() !== '' && getContentComponent()}
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  container: {
    padding: 16,
    backgroundColor: '#f0f0f0',
  },
  input: {
    height: 48,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 12,
    fontSize: 16,
    marginBottom: 12,
    backgroundColor: '#fff',
  },
  buttonMargin: {
    marginTop: 12,
  },
  storyContainer: {
    height: 200,
    marginTop: 16,
  },
});
