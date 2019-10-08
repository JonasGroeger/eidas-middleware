/*
 * Copyright (c) 2019 Governikus KG. Licensed under the EUPL, Version 1.2 or as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work except
 * in compliance with the Licence. You may obtain a copy of the Licence at:
 * http://joinup.ec.europa.eu/software/page/eupl Unless required by applicable law or agreed to in writing,
 * software distributed under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */

package de.governikus.eumw.eidasmiddleware;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.springframework.web.util.HtmlUtils;

import de.governikus.eumw.eidascommon.Utils;
import oasis.names.tc.dss._1_0.core.schema.Result;


/**
 * Helper class for web service functionality
 */
public final class WebServiceHelper
{

  private WebServiceHelper()
  {

  }

  /**
   * Return true if the result codes match the expected values
   *
   * @param result
   * @param expectedResultMajor
   * @param expectedResultMinor
   */
  public static boolean checkResult(Result result, String expectedResultMajor, String expectedResultMinor)
  {
    return expectedResultMajor != null && expectedResultMajor.equals(result.getResultMajor())
           && ((expectedResultMinor == null && result.getResultMinor() == null)
               || result.getResultMinor().equals(expectedResultMinor));
  }

  /**
   * Return a HTML page which creates a POST forward to the SAML Consumer
   *
   * @param saml
   * @param relayState
   * @throws IOException
   */
  public static String createForwardToConsumer(byte[] saml, String relayState, String htmlConfig)
    throws IOException
  {
    String html = null;
    if (htmlConfig != null && !htmlConfig.trim().isEmpty())
    {
      html = htmlConfig;
    }
    else
    {
      html = Utils.readFromStream(WebServiceHelper.class.getResourceAsStream("forwardToConsumer.html"));
    }
    html = html.replace("${SAML}", DatatypeConverter.printBase64Binary(saml));
    if (relayState == null)
    {
      html = html.replace("${RELAY_STATE}", "");
    }
    else
    {
      relayState = HtmlUtils.htmlEscape(relayState, Utils.ENCODING);
      html = html.replace("${RELAY_STATE}",
                          "<input type=\"hidden\" name=\"RelayState\" value=\"" + relayState + "\" />");
    }



    return html;
  }
}
