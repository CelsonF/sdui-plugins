# Usage Examples and Tutorials

## Overview

This document provides practical examples and tutorials for using the SDUI (Server-Driven UI) API. These examples will help you understand how to integrate with the API and implement client-side rendering of the server-driven UI components.

## API Usage Examples

### Fetching Plugins by Feature

#### Using cURL

```bash
curl -X GET "http://localhost:8080/sdui/plugins?feature=mastercard-benefits&feature=loyalty-points" -H "accept: application/json"
```

#### Using JavaScript (Fetch API)

```javascript
const fetchPlugins = async (features) => {
  const featureParams = features.map(feature => `feature=${encodeURIComponent(feature)}`).join('&');
  const response = await fetch(`http://localhost:8080/sdui/plugins?${featureParams}`, {
    method: 'GET',
    headers: {
      'Accept': 'application/json'
    }
  });
  
  if (!response.ok) {
    throw new Error(`HTTP error! Status: ${response.status}`);
  }
  
  return await response.json();
};

// Example usage
fetchPlugins(['mastercard-benefits', 'loyalty-points'])
  .then(plugins => {
    console.log('Fetched plugins:', plugins);
    // Process and render the plugins
  })
  .catch(error => {
    console.error('Error fetching plugins:', error);
  });
```

#### Using Java (HttpClient)

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SDUIClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    
    public SDUIClient(String baseUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
    }
    
    public String getPluginsByFeature(List<String> features) throws Exception {
        String featureParams = features.stream()
            .map(feature -> "feature=" + feature)
            .reduce((a, b) -> a + "&" + b)
            .orElse("");
            
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/sdui/plugins?" + featureParams))
            .header("Accept", "application/json")
            .GET()
            .build();
            
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch plugins: " + response.statusCode());
        }
        
        return response.body();
    }
    
    public static void main(String[] args) {
        try {
            SDUIClient client = new SDUIClient("http://localhost:8080");
            String plugins = client.getPluginsByFeature(List.of("mastercard-benefits", "loyalty-points"));
            System.out.println(plugins);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Client-Side Rendering Tutorials

### Rendering SDUI Components in React

Here's an example of how to render SDUI components in a React application:

```jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';

// Component to render a card benefits plugin
const CardBenefitsPlugin = ({ plugin }) => {
  const { style, modifier, benefits } = plugin;
  
  const containerStyle = {
    backgroundColor: style.backgroundColor,
    borderRadius: style.borderRadius,
    color: style.textColor,
    fontFamily: style.fontFamily,
    padding: modifier.padding,
    margin: modifier.margin,
    textAlign: modifier.alignment,
    boxShadow: style.shadow ? '0 4px 8px rgba(0,0,0,0.1)' : 'none'
  };
  
  return (
    <div style={containerStyle}>
      {benefits.map((benefitGroup, index) => (
        <div key={index} className="benefit-group">
          <h3>{benefitGroup.profile}</h3>
          <ul>
            {benefitGroup.benefits.map((benefit, i) => (
              <li key={i}>{benefit}</li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
};

// Component to render a points summary plugin
const PointsSummaryPlugin = ({ plugin }) => {
  const { style, modifier, points } = plugin;
  
  const containerStyle = {
    backgroundColor: style.backgroundColor,
    borderRadius: style.borderRadius,
    color: style.textColor,
    fontWeight: style.fontWeight,
    padding: modifier.padding,
    margin: modifier.margin,
    textAlign: modifier.alignment
  };
  
  const formattedDate = new Date(points.lastUpdated).toLocaleDateString();
  
  return (
    <div style={containerStyle}>
      <h3>Points Summary</h3>
      <p>Total: {points.total} {points.currency}</p>
      <p>Last Updated: {formattedDate}</p>
    </div>
  );
};

// Main component that fetches and renders plugins
const SDUIContainer = () => {
  const [plugins, setPlugins] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    const fetchPlugins = async () => {
      try {
        const response = await axios.get('http://localhost:8080/sdui/plugins', {
          params: {
            feature: ['mastercard-benefits', 'loyalty-points']
          }
        });
        setPlugins(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch plugins');
        setLoading(false);
      }
    };
    
    fetchPlugins();
  }, []);
  
  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  
  return (
    <div className="sdui-container">
      {plugins.map((plugin, index) => {
        switch (plugin.type) {
          case 'card-benefits-plugin':
            return <CardBenefitsPlugin key={index} plugin={plugin} />;
          case 'points-summary-plugin':
            return <PointsSummaryPlugin key={index} plugin={plugin} />;
          default:
            return <div key={index}>Unknown plugin type: {plugin.type}</div>;
        }
      })}
    </div>
  );
};

export default SDUIContainer;
```

### Rendering SDUI Components in Android (Kotlin)

Here's an example of how to render SDUI components in an Android application using Kotlin:

```kotlin
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class SDUIActivity : AppCompatActivity() {
    private lateinit var container: LinearLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdui)
        
        container = findViewById(R.id.sdui_container)
        
        fetchPlugins()
    }
    
    private fun fetchPlugins() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://10.0.2.2:8080/sdui/plugins?feature=mastercard-benefits&feature=loyalty-points")
                val response = url.readText()
                val plugins = JSONArray(response)
                
                withContext(Dispatchers.Main) {
                    renderPlugins(plugins)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            }
        }
    }
    
    private fun renderPlugins(plugins: JSONArray) {
        for (i in 0 until plugins.length()) {
            val plugin = plugins.getJSONObject(i)
            val type = plugin.getString("type")
            
            when (type) {
                "card-benefits-plugin" -> renderCardBenefitsPlugin(plugin)
                "points-summary-plugin" -> renderPointsSummaryPlugin(plugin)
                else -> {
                    val textView = TextView(this)
                    textView.text = "Unknown plugin type: $type"
                    container.addView(textView)
                }
            }
        }
    }
    
    private fun renderCardBenefitsPlugin(plugin: JSONObject) {
        val style = plugin.getJSONObject("style")
        val modifier = plugin.getJSONObject("modifier")
        val benefits = plugin.getJSONArray("benefits")
        
        val cardView = CardView(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
        cardView.layoutParams = params
        
        // Apply styling
        cardView.radius = dpToPx(style.getString("borderRadius").replace("dp", "").toFloat())
        cardView.setCardBackgroundColor(parseColor(style.getString("backgroundColor")))
        if (style.getBoolean("shadow")) {
            cardView.cardElevation = dpToPx(4f)
        }
        
        val contentLayout = LinearLayout(this)
        contentLayout.orientation = LinearLayout.VERTICAL
        contentLayout.setPadding(
            dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16)
        )
        
        for (j in 0 until benefits.length()) {
            val benefitGroup = benefits.getJSONObject(j)
            val profile = benefitGroup.getString("profile")
            val benefitsList = benefitGroup.getJSONArray("benefits")
            
            val profileTextView = TextView(this)
            profileTextView.text = profile
            profileTextView.textSize = 18f
            profileTextView.setTextColor(parseColor(style.getString("textColor")))
            contentLayout.addView(profileTextView)
            
            for (k in 0 until benefitsList.length()) {
                val benefit = benefitsList.getString(k)
                val benefitTextView = TextView(this)
                benefitTextView.text = "• $benefit"
                benefitTextView.setTextColor(parseColor(style.getString("textColor")))
                contentLayout.addView(benefitTextView)
            }
        }
        
        cardView.addView(contentLayout)
        container.addView(cardView)
    }
    
    private fun renderPointsSummaryPlugin(plugin: JSONObject) {
        val style = plugin.getJSONObject("style")
        val modifier = plugin.getJSONObject("modifier")
        val points = plugin.getJSONObject("points")
        
        val cardView = CardView(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
        cardView.layoutParams = params
        
        // Apply styling
        cardView.radius = dpToPx(style.getString("borderRadius").replace("dp", "").toFloat())
        cardView.setCardBackgroundColor(parseColor(style.getString("backgroundColor")))
        
        val contentLayout = LinearLayout(this)
        contentLayout.orientation = LinearLayout.VERTICAL
        contentLayout.setPadding(
            dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16)
        )
        
        val titleTextView = TextView(this)
        titleTextView.text = "Points Summary"
        titleTextView.textSize = 18f
        titleTextView.setTextColor(parseColor(style.getString("textColor")))
        contentLayout.addView(titleTextView)
        
        val totalTextView = TextView(this)
        totalTextView.text = "Total: ${points.getInt("total")} ${points.getString("currency")}"
        totalTextView.setTextColor(parseColor(style.getString("textColor")))
        contentLayout.addView(totalTextView)
        
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = dateFormat.parse(points.getString("lastUpdated"))
        val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date!!)
        
        val lastUpdatedTextView = TextView(this)
        lastUpdatedTextView.text = "Last Updated: $formattedDate"
        lastUpdatedTextView.setTextColor(parseColor(style.getString("textColor")))
        contentLayout.addView(lastUpdatedTextView)
        
        cardView.addView(contentLayout)
        container.addView(cardView)
    }
    
    private fun dpToPx(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
    
    private fun dpToPx(dp: Int): Int {
        return dpToPx(dp.toFloat())
    }
    
    private fun parseColor(colorString: String): Int {
        return android.graphics.Color.parseColor(colorString)
    }
}
```

## Customizing the SDUI Configuration

### Adding a New Plugin Type

To add a new plugin type to the SDUI system:

1. Create a new model class for the plugin-specific data (if needed)
2. Update the `Plugin` class to include the new data (if needed)
3. Add the new plugin definition to the `sdui-plugins.json` file

Example of adding a "carousel-plugin" to the JSON configuration:

```json
{
  "type": "container",
  "feature": "mastercard-dashboard",
  "children": [
    // ... existing plugins ...
    {
      "type": "carousel-plugin",
      "feature": "promotional-offers",
      "modifier": {
        "padding": "8dp",
        "margin": "16dp",
        "alignment": "center"
      },
      "style": {
        "backgroundColor": "#F5F5F5",
        "borderRadius": "12dp",
        "textColor": "#222222",
        "shadow": true,
        "fontFamily": "Roboto"
      },
      "items": [
        {
          "title": "Summer Promotion",
          "description": "Get 2x points on all purchases",
          "imageUrl": "https://example.com/summer.jpg",
          "actionUrl": "https://example.com/promo/summer"
        },
        {
          "title": "Travel Insurance",
          "description": "Free travel insurance with your card",
          "imageUrl": "https://example.com/travel.jpg",
          "actionUrl": "https://example.com/insurance/travel"
        }
      ]
    }
  ]
}
```

4. Update your client applications to handle the new plugin type
