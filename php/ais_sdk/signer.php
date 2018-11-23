<?php
define("BasicDateFormat", "Ymd\THis\Z");
define("Algorithm", "SDK-HMAC-SHA256");
define("HeaderXDate", "X-Sdk-Date");
define("HeaderHost", "host");
define("HeaderAuthorization", "Authorization");
define("HeaderContentSha256", "X-Sdk-Content-Sha256");


class Request
{
    public $method = "";
    public $scheme = "";
    public $host = "";
    public $uri = "";
    public $query = array();
    public $headers = array();
    public $body = "";
}

class Signer
{
    public $AppKey = "";
    public $AppSecret = "";

    function escape($string)
    {
        $entities = array("+", "%7E");
        $replacements = array("%20", "~");
        return str_replace($entities, $replacements, urlencode($string));
    }
// Build a CanonicalRequest from a regular request string
//
// CanonicalRequest =
//  HTTPRequestMethod + "\n" +
//  CanonicalURI + "\n" +
//  CanonicalQueryString + "\n" +
//  CanonicalHeaders + "\n" +
//  SignedHeaders + "\n" +
//  HexEncode(Hash(RequestPayload))
    function CanonicalRequest($r)
    {
        $CanonicalURI = $this->CanonicalURI($r);
        $CanonicalQueryString = $this->CanonicalQueryString($r);
        $canonicalHeaders = $this->CanonicalHeaders($r);
        $SignedHeaders = $this->SignedHeaders($r);
        if (@$r->headers[HeaderContentSha256]) {
            $hash = $r->headers[HeaderContentSha256];
        } else {
            $hash = hash("sha256", $r->body);
        }
        return "$r->method\n$CanonicalURI\n$CanonicalQueryString\n$canonicalHeaders\n$SignedHeaders\n$hash";
    }

// CanonicalURI returns request uri
    function CanonicalURI($r)
    {
        $pattens = explode("/", $r->uri);
        $uri = array();
        foreach ($pattens as $v) {
            array_push($uri, $this->escape($v));
        }
        $urlpath = join("/", $uri);
        if (substr($urlpath, -1) != "/") {
            $urlpath = $urlpath . "/";
        }
        return $urlpath;
    }

// CanonicalQueryString
    function CanonicalQueryString($r)
    {
        $a = array();
        foreach ($r->query as $key => $value) {
            $k = $this->escape($key);
            if ($value == "") {
                $kv = $k;
            } else {
                $kv = "$k=" . $this->escape($value);
            }
            array_push($a, $kv);
        }
        sort($a);
        return join("&", $a);
    }

// CanonicalHeaders
    function CanonicalHeaders($r)
    {
        $a = array();
        foreach ($r->headers as $key => $value) {
            array_push($a, strtolower($key) . ":" . trim($value));
        }
        sort($a);
        return join("\n", $a) . "\n";
    }

    function curlHeaders($r)
    {
        $header = array();
        foreach ($r->headers as $key => $value) {
            array_push($header, strtolower($key) . ":" . trim($value));
        }
        return $header;
    }

// SignedHeaders
    function SignedHeaders($r)
    {
        $a = array();
        foreach ($r->headers as $key => $value) {
            array_push($a, strtolower($key));
        }
        sort($a);
        return join(";", $a);
    }

// Create a "String to Sign".
    function StringToSign($canonicalRequest, $t)
    {
        date_default_timezone_set("UTC");
        $date = date(BasicDateFormat, $t);
        $hash = hash("sha256", $canonicalRequest);
        return "SDK-HMAC-SHA256\n$date\n$hash";
    }

// Create the HWS Signature.
    function SignStringToSign($stringToSign, $signingKey)
    {
        return hash_hmac("sha256", $stringToSign, $signingKey);
    }

// Get the finalized value for the "Authorization" header. The signature parameter is the output from SignStringToSign
    function AuthHeaderValue($signature, $accessKey, $signedHeaders)
    {
        return "SDK-HMAC-SHA256 Access=$accessKey, SignedHeaders=$signedHeaders, Signature=$signature";
    }

    public function Sign($r)
    {
        date_default_timezone_set("UTC");
        if (@$r->headers[HeaderXDate]) {
            $t = date_create_from_format(BasicDateFormat, $r->headers[HeaderXDate]);
        }
        if (!@$t) {
            $t = time();
            $r->headers[HeaderXDate] = date(BasicDateFormat, $t);
        }
        $queryString = $this->CanonicalQueryString($r);
        if ($queryString != "") {
            $queryString = "?" . $queryString;
        }
        $canonicalRequest = $this->CanonicalRequest($r);
        $stringToSign = $this->StringToSign($canonicalRequest, $t);
        $signature = $this->SignStringToSign($stringToSign, $this->AppSecret);
        $signedHeaders = $this->SignedHeaders($r);
        $authValue = $this->AuthHeaderValue($signature, $this->AppKey, $signedHeaders);
        $r->headers[HeaderAuthorization] = $authValue;

        $curl = curl_init();
        $url = $r->scheme . "://" . $r->host . $r->uri . $queryString;
        $headers = $this->curlHeaders($r);
        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, $r->method);
        curl_setopt($curl, CURLOPT_POSTFIELDS, $r->body);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($curl, CURLOPT_NOBODY, FALSE);
        return $curl;
    }
}
